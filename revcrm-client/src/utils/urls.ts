
export function buildQueryString(obj: any) {
    const parts: string[] = []
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            parts.push(encodeURIComponent(key) + "=" + encodeURIComponent(obj[key]))
        }
    }
    return parts.join("&")
}

export function queryStringToObject(queryString: string) {
    const params = new URLSearchParams(queryString)
    const result: {[key: string]: any} = {}
    for (const entry of params.entries()) {
      result[entry[0]] = entry[1]
    }
    return result
}
