
import * as rev from 'rev-models';
import { RevCRMModel } from '../RevCRMModel';
import { ApiOperations } from 'rev-api';

@ApiOperations(['read'])
export class User extends RevCRMModel<User> {

    @rev.AutoNumberField()
        id: number;
    @rev.EmailField({ label: 'Email', required: false })
        email: string;
    @rev.BooleanField({ label: 'Active?' })
        active: boolean;

}
