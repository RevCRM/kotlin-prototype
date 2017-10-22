
import * as rev from 'rev-models';
import { ApiOperations, ApiMethod } from 'rev-api';
import { IMethodContext } from 'rev-models';

@ApiOperations(['create', 'read', 'update', 'remove'])
export class Company {

    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.TextField()
        name: string;
    @rev.URLField()
        url: string;

    @ApiMethod({
        args: [
            new rev.fields.TextField('invoiceNumber'),
            new rev.fields.IntegerField('copies')
        ]
    })
    printInvoice(ctx: IMethodContext<any>) {
        // Do clever stuff
    }

    validate(vc: rev.IValidationContext) {
        if (this.name.toLowerCase().includes('test')) {
            vc.result.addFieldError('name', 'Name cannot include the word "test"');
        }
    }

}
