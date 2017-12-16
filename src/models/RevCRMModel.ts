
export class RevCRMModel<T> {
    constructor(data?: Partial<T>) {
        Object.assign(this, data);
    }
}
