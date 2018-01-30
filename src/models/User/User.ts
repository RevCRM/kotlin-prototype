
import * as rev from 'rev-models';
import { EntityModel } from '../BaseModels';
import { ApiOperations } from 'rev-api/lib/decorators';

@ApiOperations(['read'])
export class User extends EntityModel<User> {

    @rev.EmailField({ label: 'Email', required: false })
        email: string;
    @rev.BooleanField({ label: 'Active?' })
        active: boolean;

}
