
import * as rev from 'rev-models';
import { EntityModel } from '../BaseModels';

export const ACCOUNT_TYPES = [
    ['organisation', 'Organisation'],
    ['contact', 'Contact']
];

export class Account extends EntityModel<Account> {

    @rev.SelectField({ label: 'Type', selection: ACCOUNT_TYPES })
        type: string = 'organisation';
    @rev.TextField({ label: 'Name' })
        name: string;
    @rev.TextField({ label: 'Account Code' })
        code: string;

    @rev.TextField({ label: 'Phone', required: false })
        phone: string;
    @rev.TextField({ label: 'Mobile', required: false })
        mobile: string;
    @rev.TextField({ label: 'E-mail', required: false })
        email: string;
    @rev.URLField({ label: 'Website', required: false })
        website: string;

    validate(vc: rev.IValidationContext) {
        if (this.name && this.name.toLowerCase().includes('test')) {
            vc.result.addFieldError('name', 'Name cannot include the word "test"');
        }
    }

}
