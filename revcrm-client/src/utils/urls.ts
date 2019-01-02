
export function buildQueryString(obj: any) {
    const parts: string[] = []
    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            parts.push(encodeURIComponent(key) + "=" + encodeURIComponent(obj[key]))
        }
    }
    return parts.join("&")
}
