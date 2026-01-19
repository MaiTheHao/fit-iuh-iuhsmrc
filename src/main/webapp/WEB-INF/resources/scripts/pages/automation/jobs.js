class JobManager {
	static init(contextPath) {
		this.contextPath = contextPath;
		// Khởi tạo HttpClient với base path api/v1/
		this.apiClient = new HttpClient(`${contextPath}api/v1/`);

		// SỬ DỤNG SERVICE MỚI (AutomationApiService)
		// Lưu ý: Đảm bảo file automation_api_v1.js đã chứa class AutomationApiService
		this.automationService = new AutomationApiService(this.apiClient);
		this.table = null;

		this.initDataTable();
		this.initPickers();
		this.bindEvents();
	}

	static initPickers() {
		if ($('#startTimePicker').length === 0) {
			console.warn('⚠️ UI Elements missing.');
			return;
		}

		$('#startTimePicker').daterangepicker({
			singleDatePicker: true,
			timePicker: true,
			timePicker24Hour: true,
			timePickerSeconds: false,
			autoApply: true,
			locale: { format: 'DD/MM/YYYY HH:mm' },
			opens: 'right',
			drops: 'auto',
		});
	}

	static bindEvents() {
		$('#btnCreateAutomation').on('click', () => this.openModal());
		$('#btnSaveAutomation').on('click', () => this.handleSave());
		$('#btnReloadAll').on('click', () => this.handleReloadSystem());

		$('#scheduleType').on('change', (e) => this.updateFormUI(e.target.value));

		$(document).on('click', '.day-cell', function () {
			$(this).toggleClass('active btn-primary text-white btn-outline-light text-dark');
		});

		// Reset Month Grid
		$('#btnResetMonth').on('click', () => {
			$('.day-cell').removeClass('active btn-primary text-white').addClass('btn-outline-light text-dark');
		});

		// Table Actions
		const tbody = $('#automationsTable tbody');
		tbody.on('click', '.btn-edit', (e) => this.handleEdit($(e.currentTarget).data('id')));
		tbody.on('click', '.btn-delete', (e) => this.handleDelete($(e.currentTarget).data('id')));

		// Toggle Switch Status
		tbody.on('change', '.toggle-status', (e) => {
			const $chk = $(e.currentTarget);
			this.handleToggleStatus($chk.data('id'), $chk.is(':checked'), $chk);
		});
	}

	static updateFormUI(type) {
		$('.schedule-option').addClass('d-none');
		const $desc = $('#patternDesc');

		if (type === 'ONCE') {
			$('#opt-simple').removeClass('d-none');
			$('#simpleMessage').text('Task runs once at the Start Time.');
			$desc.text('Single execution');
		} else if (type === 'DAILY') {
			$('#opt-simple').removeClass('d-none');
			$('#simpleMessage').text('Task repeats every day at the time specified.');
			$desc.text('Repeats every day');
		} else if (type === 'WEEKLY') {
			$('#opt-weekly').removeClass('d-none');
			$desc.text('Repeats on specific weekdays');
		} else if (type === 'MONTHLY') {
			$('#opt-monthly').removeClass('d-none');
			$desc.text('Repeats on specific days of the month');
		}
	}

	static openModal(data = null) {
		const isEdit = !!data;
		const $form = $('#automationForm');
		$form[0].reset();
		$('#autoId').val('');

		$('input[name="weekDays"]').parent().removeClass('active');
		$('.day-cell').removeClass('active btn-primary text-white').addClass('btn-outline-light text-dark');

		// Mặc định Active = true khi tạo mới
		$('#autoIsActive').prop('checked', true);

		const now = moment().add(5, 'minutes');

		if (!isEdit) {
			$('#modalTitle').html('<i class="fas fa-plus-circle mr-2"></i> New Schedule');
			$('#scheduleType').val('DAILY').trigger('change');
			$('#startTimePicker').data('daterangepicker').setStartDate(now);
			$('#startTimePicker').data('daterangepicker').setEndDate(now);
		} else {
			$('#modalTitle').html(`<i class="fas fa-edit mr-2"></i> Edit Schedule #${data.id}`);
			$('#autoId').val(data.id);
			$('#autoName').val(data.name);
			$('#autoDescription').val(data.description);
			$('#autoIsActive').prop('checked', data.isActive);

			// Logic parse Cron để hiển thị ngược lại lên UI
			// Yêu cầu CronUtils phải có function fromCron
			const parsed = CronUtils.fromCron(data.cronExpression);
			$('#scheduleType').val(parsed.type).trigger('change');

			// Set thời gian cho Picker (lấy giờ phút từ cron)
			$('#startTimePicker').data('daterangepicker').setStartDate(parsed.moment);
			$('#startTimePicker').data('daterangepicker').setEndDate(parsed.moment);

			if (parsed.type === 'WEEKLY' && parsed.daysOfWeek) {
				parsed.daysOfWeek.forEach((day) => {
					$(`input[name="weekDays"][value="${day}"]`).prop('checked', true).parent().addClass('active');
				});
			} else if (parsed.type === 'MONTHLY' && parsed.dayOfMonth) {
				const days = parsed.dayOfMonth.split(',');
				days.forEach((d) => {
					$(`.day-cell[data-val="${d}"]`).removeClass('btn-outline-light text-dark').addClass('active btn-primary text-white');
				});
			}
		}
		$('#automationModal').modal('show');
	}

	static async handleSave() {
		if (!$('#autoName').val()) {
			notify.warning('Task Name is required');
			return;
		}

		const type = $('#scheduleType').val();
		const picker = $('#startTimePicker').data('daterangepicker');
		if (!picker) return;

		const startMoment = picker.startDate;
		let daysOfWeek = [];
		let dayOfMonthStr = '';

		if (type === 'WEEKLY') {
			$('input[name="weekDays"]:checked').each(function () {
				daysOfWeek.push($(this).val());
			});
			if (daysOfWeek.length === 0) {
				notify.warning('Select at least one weekday.');
				return;
			}
		} else if (type === 'MONTHLY') {
			const selectedDays = [];
			$('.day-cell.active').each(function () {
				selectedDays.push($(this).data('val'));
			});
			if (selectedDays.length === 0) {
				notify.warning('Select at least one day of the month.');
				return;
			}
			dayOfMonthStr = selectedDays.join(',');
		}

		// Sinh chuỗi Cron từ cấu hình UI
		const cronConfig = {
			type: type,
			moment: startMoment,
			daysOfWeek: daysOfWeek,
			dayOfMonth: dayOfMonthStr,
		};
		const cronString = CronUtils.toCron(cronConfig);

		// Payload chuẩn theo CreateAutomationDto/UpdateAutomationDto
		const payload = {
			name: $('#autoName').val(),
			cronExpression: cronString, // Backend chỉ cần cron string
			description: $('#autoDescription').val(),
			isActive: $('#autoIsActive').is(':checked'),
		};

		const id = $('#autoId').val();
		try {
			if (id) {
				await this.automationService.update(id, payload);
				notify.success('Schedule updated successfully');
			} else {
				await this.automationService.create(payload);
				notify.success('Schedule created successfully');
			}

			$('#automationModal').modal('hide');
			this.table.ajax.reload();
		} catch (error) {
			// Xử lý lỗi từ ApiResponse nếu có
			const msg = error.responseJSON?.message || error.message || 'Unknown error';
			notify.error('Error: ' + msg);
		}
	}

	static async handleEdit(id) {
		try {
			const res = await this.automationService.getById(id);
			if (res.data) this.openModal(res.data);
		} catch (e) {
			notify.error('Failed to load details');
		}
	}

	static initDataTable() {
		this.table = $('#automationsTable').DataTable({
			processing: true,
			serverSide: true,
			ajax: async (data, callback) => {
				try {
					const page = Math.floor(data.start / data.length);
					const size = data.length;
					const res = await this.automationService.getAll(page, size);

					// Mapping response từ PaginatedResponse
					callback({
						recordsTotal: res.data.totalElements,
						recordsFiltered: res.data.totalElements,
						data: res.data.content || [],
					});
				} catch (error) {
					console.error(error);
					callback({ data: [] });
				}
			},
			columns: [
				{ data: 'id', width: '50px', className: 'align-middle' },
				{ data: 'name', className: 'align-middle', render: (d) => `<span class="font-weight-bold text-dark">${d}</span>` },
				{
					data: 'cronExpression',
					className: 'align-middle',
					render: (cron) => {
						// Hiển thị cron đẹp hơn nếu có CronUtils
						return CronUtils.formatDisplay ? CronUtils.formatDisplay(cron) : cron;
					},
				},
				{
					data: 'isActive',
					className: 'text-center align-middle',
					render: (active, type, row) => `
                        <div class="custom-control custom-switch">
                            <input type="checkbox" class="custom-control-input toggle-status" id="status_${row.id}" data-id="${row.id}" ${active ? 'checked' : ''}>
                            <label class="custom-control-label" for="status_${row.id}"></label>
                        </div>`,
				},
				{
					data: null,
					className: 'text-center align-middle',
					render: (data, type, row) => `
                        <div class="btn-group">
                            <button class="btn btn-sm btn-default btn-edit" title="Edit Info" data-id="${row.id}">
                                <i class="fas fa-pen text-primary"></i>
                            </button>
                            <a href="automations/${row.id}/design" class="btn btn-sm btn-default" title="Design Actions">
                                <i class="fas fa-cogs text-warning"></i>
                            </a>
                            <button class="btn btn-sm btn-default btn-delete" title="Delete" data-id="${row.id}">
                                <i class="fas fa-trash text-danger"></i>
                            </button>
                        </div>
                    `,
				},
			],
			order: [[0, 'desc']],
			language: { emptyTable: 'No automations found.' },
		});
	}

	static handleDelete(id) {
		if (confirm('Are you sure you want to delete this automation? All actions will be removed.')) {
			this.automationService.delete(id).then(() => {
				notify.success('Deleted successfully');
				this.table.ajax.reload();
			});
		}
	}

	static handleToggleStatus(id, isActive, $chk) {
		this.automationService
			.toggleStatus(id, isActive)
			.then(() => notify.success(`Automation ${isActive ? 'enabled' : 'disabled'}`))
			.catch(() => {
				$chk.prop('checked', !isActive); // Revert UI on error
				notify.error('Failed to update status');
			});
	}

	static handleReloadSystem() {
		if (confirm('Reload quartz scheduler system?')) {
			this.automationService
				.reloadSystem()
				.then(() => notify.success('System reloaded'))
				.catch((err) => notify.error('Reload failed'));
		}
	}
}

window.JobManager = JobManager;
