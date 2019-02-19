
// https://gist.github.com/Billy-/d94b65998501736bfe6521eadc1ab538

export function omitDeep<T extends any>(obj: T, key: string): T {
    if (Array.isArray(obj)) return omitDeepArrayWalk(obj, key)
    const keys = Object.keys(obj)
    const newObj: any = {}
    keys.forEach((i) => {
        if (i !== key) {
            const val = obj[i]
            if (Array.isArray(val)) newObj[i] = omitDeepArrayWalk(val, key)
            else if (typeof val === "object" && val !== null) newObj[i] = omitDeep(val, key)
            else newObj[i] = val
        }
    })
    return newObj
}

export function omitDeepArrayWalk<T extends any>(arr: T, key: string): T {
    return arr.map((val: any) => {
        if (Array.isArray(val)) return omitDeepArrayWalk(val, key)
        else if (typeof val === "object") return omitDeep(val, key)
        return val
    })
}

export function isDefined(value: any): boolean {
    return (typeof value != "undefined")
}
