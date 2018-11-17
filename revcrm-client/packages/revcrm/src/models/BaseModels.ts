
import * as rev from 'rev-models';
import { User } from './User/User';

export class BaseModel<T> {
    constructor(data?: Partial<T>) {
        Object.assign(this, data);
    }
}

export class EntityModel<T> {
    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.RelatedModel({ model: 'User', required: false })
        createdBy: User;
    @rev.DateTimeField({ required: false })
        createdDate: string;
    @rev.RelatedModel({ model: 'User', required: false })
        updatedBy: User;
    @rev.DateTimeField({ required: false })
        updatedDate: string;

    constructor(data?: Partial<T>) {
        Object.assign(this, data);
    }
}
