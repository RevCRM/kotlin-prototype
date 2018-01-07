
import * as rev from 'rev-models';
import { ApiOperations, ApiMethod } from 'rev-api/lib/decorators';
import { Account as AccountBase } from './Account';

@ApiOperations(['create', 'read', 'update', 'remove'])
export class Account extends AccountBase {

    @ApiMethod({
        args: [
            new rev.fields.TextField('invoiceNumber'),
            new rev.fields.IntegerField('copies')
        ]
    })
    printInvoice(ctx: rev.IMethodContext<any>) {
        // Do clever stuff
    }

}
