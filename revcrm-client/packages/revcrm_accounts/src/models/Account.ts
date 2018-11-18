
import * as rev from 'rev-models';
import { EntityModel } from 'revcrm/lib/models/BaseModels';
import { Address } from './Address';

export const ACCOUNT_TYPES = [
    ['organisation', 'Organisation'],
    ['contact', 'Contact']
];

export const TITLES = [
    ['Mr', 'Mr'],
    ['Mrs', 'Mrs'],
    ['Miss', 'Miss'],
    ['Ms', 'Ms'],
    ['Dr', 'Dr'],
    ['Sir', 'Sir'],
    ['Prof', 'Prof'],
];

export const TAGS = [
    ['lead', 'Lead'],
    ['prospect', 'Prospect'],
    ['client', 'Client'],
    ['supplier', 'Supplier'],
    ['recruiter', 'Recruiter'],
];

export class Account extends EntityModel<Account> {

    @rev.SelectField({ label: 'Type', selection: ACCOUNT_TYPES })
        type: string;
    @rev.MultiSelectField({ label: 'Tags', selection: TAGS, required: false })
        tags?: string[];
    @rev.TextField({ label: 'Account Code', required: false })
        code?: string;
    @rev.TextField({ label: 'Organisation Name', required: false })
        org_name: string;
    @rev.SelectField({ label: 'Title', selection: TITLES, required: false })
        title: string;
    @rev.TextField({ label: 'First Name', required: false })
        first_name: string;
    @rev.TextField({ label: 'Last Name', required: false })
        last_name: string;

    @rev.TextField({ label: 'Name' })
        get name() {
            return this.toString();
        }

    @rev.TextField({ label: 'Phone', required: false })
        phone: string;
    @rev.TextField({ label: 'Mobile', required: false })
        mobile: string;
    @rev.TextField({ label: 'Fax', required: false })
        fax: string;
    @rev.TextField({ label: 'E-mail', required: false })
        email: string;
    @rev.URLField({ label: 'Website', required: false })
        website: string;
    @rev.TextField({ label: 'Notes', multiLine: true, required: false })
        notes: string;

    @rev.RelatedModel({ model: 'Address' })
        primary_address: Address;

    defaults() {
        this.type = 'organisation';
    }

    validate(vc: rev.IValidationContext) {
        if (this.type == 'organisation' && !this.org_name) {
            vc.result.addFieldError('org_name', 'Organisation Name is required');
        }
        if (this.type == 'contact' && !this.last_name) {
            vc.result.addFieldError('last_name', 'Last Name is required');
        }
    }

    toString() {
        return this.type == 'organisation'
                ? this.org_name
                : (this.first_name || '') + ' ' + this.last_name;
    }

}