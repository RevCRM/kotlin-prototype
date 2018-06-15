
import * as rev from 'rev-models';
import { EntityModel } from 'revcrm/lib/models/BaseModels';

export const ADDRESS_TYPES = [
    ['invoice', 'Invoice Address'],
    ['delivery', 'Delivery Address']
];

export const COUNTRIES = [
    ['AUS', 'Australia'],
    ['NZL', 'New Zealand'],
    ['GBR', 'United Kingdom'],
];

export class Address extends EntityModel<Address> {

    @rev.TextField({ label: 'Address Name' })  // e.g. Head Office
        name: string;
    @rev.MultiSelectField({ label: 'Address Type', selection: ADDRESS_TYPES })
        type: string;
    @rev.TextField({ label: 'Address Line 1' })
        address1: string;
    @rev.TextField({ label: 'Address Line 2' })
        address2: string;
    @rev.TextField({ label: 'City' })
        city: string;
    @rev.TextField({ label: 'Region' })
        region: string;
    @rev.SelectField({ label: 'Country', selection: COUNTRIES })
        country: string;

}
