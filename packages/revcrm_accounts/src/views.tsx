
import * as React from 'react';
import Home from '@material-ui/icons/Home';
import { Field, SearchField, ListView } from 'rev-ui';
import { ViewManager } from 'revcrm/lib/client';
import { CRMListView, CRMFormView, Panel, CRMViewContext, Tabs } from 'revcrm/lib/views';
import { AccountLink } from './models/AccountLink';

export function registerViews(views: ViewManager) {

    // Account views
    views.registerView({
        name: 'account_list',
        model: 'Account',
        component: (
            <CRMListView
                searchFields={<>
                    <SearchField name="org_name" colspan={4} />
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
                <CRMViewContext.Consumer>{(ctx) => (
                    <Tabs colspan={12} tabs={[
                        {
                            label: 'Related Accounts',
                            component: <ListView
                                title=" "
                                model="AccountLink"
                                where={{
                                    parent: ctx.primaryKeyValue
                                }}
                                fields={[
                                    'child',
                                    'child_relationship',
                                ]}
                                related={[
                                    'child'
                                ]}
                                onItemPress={(record: AccountLink) => ctx.changePerspective('accounts', 'form', { id: record.child.id })}
                            />
                        },
                        {
                            label: 'Notes',
                            grid: true,
                            component: <Field name="notes" colspan={12} />
                        },
                    ]} />
                )}</CRMViewContext.Consumer>
            </CRMFormView>
        )
    });
    views.registerView({
        name: 'account_contact_form',
        model: 'Account',
        component: (
            <CRMFormView>
                <Panel title="Contact Summary" colspan={12}>
                    <Field name="type" />
                    <Field name="tags" />
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
                <Panel title="Address & Nodes">
                    <Field name="primary_address" colspan={12} />
                    <Field name="notes" colspan={12} />
                </Panel>
            </CRMFormView>
        )
    });

    // Account Link views
    views.registerView({
        name: 'accountlink_list',
        model: 'AccountLink',
        component: (
            <CRMListView
                searchFields={<>
                    <SearchField name="parent" />
                    <SearchField name="parent_relationship" />
                    <SearchField name="child" />
                    <SearchField name="child_relationship" />
                </>}
                fields={[
                    'parent',
                    'parent_relationship',
                    'child',
                    'child_relationship',
                ]}
                related={[
                    'parent',
                    'child'
                ]}
                detailView="accounts/link"
            />
        )
    });
    views.registerView({
        name: 'accountlink_form',
        model: 'AccountLink',
        component: (
            <CRMFormView>
                <Panel title="Account Link" colspan={12}>
                    <Field name="parent" />
                    <Field name="child" />
                    <Field name="parent_relationship" />
                    <Field name="child_relationship" />
                </Panel>
            </CRMFormView>
        )
    });

    views.registerPerspective({
        name: 'accounts',
        title: 'Accounts',
        views: {
            list: 'account_list',
            form: 'account_form',
            contact: 'account_contact_form',
            links: 'accountlink_list',
            link: 'accountlink_form'
        }
    });

    views.registerMenu({
        text: 'Accounts',
        url: '/accounts/list',
        icon: <Home />
    });
    views.registerMenu({
        text: 'Account Links',
        url: '/accounts/links',
        icon: <Home />
    });

 }
