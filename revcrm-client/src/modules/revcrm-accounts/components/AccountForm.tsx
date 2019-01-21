
import * as React from "react"
import { Panel } from "../../../components/views/widgets/Panel"
import { Field } from "../../../components/fields/Field"
import { FormView } from "../../../components/views/FormView"

export const AccountForm = () => (
    <FormView entity="Account">
        <Panel title="Account Summary" colspan={12}>
            <Field name="org_name" colspan={9} />
            <Field name="is_org" colspan={3} />
            <Field name="code" colspan={2} />
            <Field name="tags" colspan={5} />
            <Field name="source" colspan={5} />
        </Panel>
        <Panel title="Contact Details">
            <Field name="title" colspan={2} />
            <Field name="first_name" colspan={4} />
            <Field name="last_name" colspan={6} />
            <Field name="phone" />
            <Field name="email" />
            <Field name="mobile" />
            <Field name="website" />
        </Panel>
        <Panel title="Notes">
            <Field name="notes" colspan={12} />
        </Panel>
    </FormView>
)
