
import * as rev from 'rev-models';
import { User as UserModel } from './User';

export class User extends UserModel {

    @rev.TextField({ expose: false } as any)
        secret_field: string;

    validate(vc: any) {
        super.validate(vc);

    }
}
