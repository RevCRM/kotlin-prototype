
import * as rev from 'rev-models';
import { EntityModel } from 'revcrm/lib/models/BaseModels';
import { ApiOperations } from 'rev-api/lib/decorators';
import { COUNTRIES } from './countries';

@ApiOperations(['create', 'read', 'update', 'remove'])
export class Address extends EntityModel<Address> {

    @rev.TextField({ label: 'Address Name' })  // e.g. Head Office
        name: string;
    @rev.TextField({ label: 'Address Line 1' })
        address1: string;
    @rev.TextField({ label: 'Address Line 2', required: false })
        address2?: string;
    @rev.TextField({ label: 'City' })
        city: string;
    @rev.TextField({ label: 'Region' })
        region: string;
    @rev.TextField({ label: 'Postal Code' })
        postal_code: string;
    @rev.SelectField({ label: 'Country', selection: COUNTRIES })
        country: string;

}
