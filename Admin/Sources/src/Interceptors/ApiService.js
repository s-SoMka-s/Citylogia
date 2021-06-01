import axios from 'axios'

export class ApiService {
    constructor() {
        this.client = axios.create({
            baseURL: `http://35.209.124.144:8000/api`,
            //baseURL: `http://localhost:5000/api`,
            timeout: 10000,
        })

        this.client.interceptors.request.use(
            (request) => this.#processRequst(request),
            (error) => this.proceessError(error)
        )

        this.client.interceptors.response.use(
            (response) => this.#processResponse(response),
            (error) => this.proceessError(error)
        )
    }

    login(data) {
        return this.client.post('/Auth/Email', data)
    }

    togglePlace(id, newValue) {
        const data = {
            is_approved: newValue,
        }
        return this.client.put(`/Places/${id}`, data)
    }

    getSummary() {
        return this.client.get('/Summary')
    }

    takePlaces(
        take = null,
        skip = null,
        onlyApproved = false,
        onlyNotReviewed = false
    ) {
        const params = []

        if (take !== null) {
            params.push(`take=${take}`)
        }

        if (skip !== null) {
            params.push(`skip=${skip}`)
        }

        params.push(`only_approved=${onlyApproved}`)
        params.push(`only_not_reviewed=${onlyNotReviewed}`)

        return this.client.get(`/Places?${params.join('&')}`)
    }

    #processResponse(response) {
        if (response.data === null || response.data === '') {
            throw 'Error response'
        }

        if (response.data.status_code !== 200) {
            throw `Status code not 200! Status code: ${response.data.status_code}`
        }

        // get response data
        return response.data.data
    }

    #processRequst(request) {
        if (this.#canSkip(request?.url)) {
            console.log('skipped')
            return request
        }

        console.log('token added')
        request.headers.authorization = 'Bearer my secret token'
        return request
    }

    #canSkip(url) {
        const urls = ['/Auth/Email']
        return urls.some((u) => url.match(url))
    }
}
