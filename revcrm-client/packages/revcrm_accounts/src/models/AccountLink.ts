
import * as rev from 'rev-models';
import { EntityModel } from 'revcrm/lib/models/BaseModels';
import { ApiOperations } from 'rev-api/lib/decorators';

@ApiOperations(['create', 'read', 'update', 'remove'])
export class AccountLink extends EntityModel<AccountLink> {

    @rev.RelatedModel({ model: 'Account', label: 'Parent Account' })
        parent: Account;
    @rev.TextField({ label: 'Parent Relationship', required: false })
        parent_relationship: string;
    @rev.RelatedModel({ model: 'Account', label: 'Child Account' })
        child: Account;
    @rev.TextField({ label: 'Child Relationship', required: false })
        child_relationship: string;

}
