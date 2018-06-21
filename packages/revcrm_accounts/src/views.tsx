
import * as React from 'react';
import { Field, SearchField } from 'rev-ui';
import { ViewManager } from 'revcrm/lib/client';
import { CRMListView, CRMFormView, Panel } from 'revcrm/lib/views';

export function registerViews(views: ViewManager) {
    views.registerPerspective({
        name: 'accounts',
        title: 'Accounts',
        views: {
            list: 'account_list',
            form: 'account_form'
        }
    });

    views.registerView({
        name: 'account_list',
        model: 'Account',
        component: (
            <CRMListView
                searchFields={<>
                    <SearchField name="name" colspan={4} />
                    <SearchField name="tags" colspan={4} />
                    <SearchField name="type" colspan={4} />
                </>}
                fields={[
                    'type',
                    'name',
                    'tags',
                    'phone',
                    'email',
                    'website'
                ]}
                detailView="accounts/form"
            />
        )
    });

    views.registerView({
        name: 'account_form',
        model: 'Account',
        related: ['primary_address'],
        component: (
            <CRMFormView>
                <Panel title="Account Summary" colspan={12}>
                    <Field name="type" />
                    <Field name="tags" />
                    <Field name="org_name" />
                    <Field name="code" />
                    <Field name="title" colspan={2} />
                    <Field name="first_name" colspan={5} />
                    <Field name="last_name" colspan={5} />
                </Panel>
                <Panel title="Contact Details">
                    <Field name="phone" colspan={12} />
                    <Field name="fax" colspan={12} />
                    <Field name="mobile" colspan={12} />
                    <Field name="email" colspan={12} />
                    <Field name="website" colspan={12} />
                </Panel>
                <Panel title="Address">
                    <Field name="primary_address.name" colspan={12} />
                    <Field name="primary_address.address1" colspan={12} />
                    <Field name="primary_address.address2" colspan={12} />
                    <Field name="primary_address.city" />
                    <Field name="primary_address.postal_code" />
                    <Field name="primary_address.region" colspan={12} />
                    <Field name="primary_address.country" colspan={12} />
                </Panel>
                <Panel colspan={12}>
                    <Field name="notes" colspan={12} />
                </Panel>
            </CRMFormView>
        )
    });
}
