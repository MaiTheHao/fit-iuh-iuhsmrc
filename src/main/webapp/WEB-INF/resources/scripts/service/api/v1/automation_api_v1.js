class AutomationApiService {
	constructor(httpClient) {
		this.client = httpClient || new HttpClient();
		this.baseUrl = 'automations';
	}

	async getAll(page = 0, size = 20) {
		try {
			return await this.client.get(this.baseUrl, { page, size });
		} catch (error) {
			this._handleError('fetch automations', error);
		}
	}

	async getById(id) {
		try {
			return await this.client.get(`${this.baseUrl}/${id}`);
		} catch (error) {
			this._handleError(`fetch automation ${id}`, error);
		}
	}

	async create(dto) {
		try {
			return await this.client.post(this.baseUrl, dto);
		} catch (error) {
			this._handleError('create automation', error);
		}
	}

	async update(id, dto) {
		try {
			return await this.client.put(`${this.baseUrl}/${id}`, dto);
		} catch (error) {
			this._handleError(`update automation ${id}`, error);
		}
	}

	async delete(id) {
		try {
			return await this.client.delete(`${this.baseUrl}/${id}`);
		} catch (error) {
			this._handleError(`delete automation ${id}`, error);
		}
	}

	async getActive() {
		try {
			return await this.client.get(`${this.baseUrl}/active`);
		} catch (error) {
			this._handleError('fetch active automations', error);
		}
	}

	async getActions(automationId) {
		try {
			return await this.client.get(`${this.baseUrl}/${automationId}/actions`);
		} catch (error) {
			this._handleError(`fetch actions for automation ${automationId}`, error);
		}
	}

	async addAction(automationId, dto) {
		try {
			return await this.client.post(`${this.baseUrl}/${automationId}/actions`, dto);
		} catch (error) {
			this._handleError(`add action to automation ${automationId}`, error);
		}
	}

	async updateAction(actionId, dto) {
		try {
			return await this.client.put(`${this.baseUrl}/actions/${actionId}`, dto);
		} catch (error) {
			this._handleError(`update action ${actionId}`, error);
		}
	}

	async removeAction(actionId) {
		try {
			return await this.client.delete(`${this.baseUrl}/actions/${actionId}`);
		} catch (error) {
			this._handleError(`remove action ${actionId}`, error);
		}
	}

	async toggleStatus(id, isActive) {
		try {
			const url = `${this.baseUrl}/${id}/status`;
			const params = { isActive };

			if (this.client.patch) {
				return await this.client.patch(url, null, { params });
			} else {
				return await this.client.request('PATCH', `${url}?isActive=${isActive}`);
			}
		} catch (error) {
			this._handleError(`toggle status ${id}`, error);
		}
	}

	async execute(id) {
		try {
			return await this.client.post(`${this.baseUrl}/${id}/execute`);
		} catch (error) {
			this._handleError(`execute automation ${id}`, error);
		}
	}

	async reloadSystem() {
		try {
			return await this.client.post(`${this.baseUrl}/reload-job`);
		} catch (error) {
			this._handleError('reload system jobs', error);
		}
	}

	_handleError(action, error) {
		console.error(`[AutomationApiService] Failed to ${action}:`, error);
		throw error;
	}
}

window.AutomationApiService = AutomationApiService;
