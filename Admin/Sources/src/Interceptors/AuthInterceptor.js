import axios from 'axios'

axios.interceptors.request.use((request) => processRequst(request))

function processRequst(request) {
    if (canSkip(request?.url)) {
        console.log('skipped')
        return request
    }

    console.log('token added')
    request.headers.authorization = 'Bearer my secret token'
    return request
}

function canSkip(url) {
    const urls = ['/Auth/Email']
    return urls.some((u) => url.match(url))
}
