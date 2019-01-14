
import * as React from "react"
import { Panel } from "../../../components/views/widgets/Panel"
import { Field } from "../../../components/fields/Field"
import { FormView } from "../../../components/views/FormView"

export const AccountForm = () => (
    <FormView entity="Account">
        <Panel title="Account Summary" colspan={12}>
            <Field name="is_org" />
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
            <h3>Related records TODO</h3>
            {/* <Field name="primary_address.name" colspan={12} />
            <Field name="primary_address.address1" colspan={12} />
            <Field name="primary_address.address2" colspan={12} />
            <Field name="primary_address.city" />
            <Field name="primary_address.postal_code" />
            <Field name="primary_address.region" colspan={12} />
            <Field name="primary_address.country" colspan={12} /> */}
        </Panel>
    </FormView>
)
