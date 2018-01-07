
import * as rev from 'rev-models';
import { ApiOperations, ApiMethod } from 'rev-api';
import { RevCRMModel } from '../RevCRMModel';

@ApiOperations(['create', 'read', 'update', 'remove'])
export class Account extends RevCRMModel<Account> {

    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.TextField({ label: 'Account Name' })
        name: string;
    @rev.TextField({ label: 'Account Code' })
        code: string;
    @rev.URLField({ label: 'Website' })
        url: string;

    @ApiMethod({
        args: [
            new rev.fields.TextField('invoiceNumber'),
            new rev.fields.IntegerField('copies')
        ]
    })
    printInvoice(ctx: rev.IMethodContext<any>) {
        // Do clever stuff
    }

    validate(vc: rev.IValidationContext) {
        if (this.name.toLowerCase().includes('test')) {
            vc.result.addFieldError('name', 'Name cannot include the word "test"');
        }
    }

}
