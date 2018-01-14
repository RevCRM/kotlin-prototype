
import * as rev from 'rev-models';
import { RevCRMModel } from '../RevCRMModel';

export class Account extends RevCRMModel<Account> {

    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.TextField({ label: 'Account Name' })
        name: string;
    @rev.TextField({ label: 'Account Code' })
        code: string;
    @rev.URLField({ label: 'Website' })
        url: string;

    validate(vc: rev.IValidationContext) {
        if (this.name && this.name.toLowerCase().includes('test')) {
            vc.result.addFieldError('name', 'Name cannot include the word "test"');
        }
    }

}
