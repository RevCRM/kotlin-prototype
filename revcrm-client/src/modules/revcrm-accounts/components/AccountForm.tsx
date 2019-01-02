
import * as React from "react"
import { Typography, Paper, Icon, Theme, createStyles, withStyles, WithStyles, IconButton, Button } from "@material-ui/core"
import { Panel } from "../../../components/views/widgets/Panel"
import { Field } from "../../../components/fields/Field"
import { FormView } from "../../../components/views/FormView"

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    formHeader: {
        padding: 12,
        height: 60,
        display: "flex",
        alignItems: "center",
        color: "#fff",
        backgroundColor: theme.palette.primary.dark,
        zIndex: theme.zIndex.appBar - 1,
        position: "relative"
    },
    backButtonContainer: {
        marginTop: -12,
        marginBottom: -12,
        marginRight: 12
    },
})

function goBack() {
    history.go(-1)
}

export const AccountForm = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <div className={props.classes.root}>
        <Paper square className={props.classes.formHeader}>
            <div className={props.classes.backButtonContainer}>
                <IconButton color="inherit" onClick={goBack}>
                    <Icon>arrow_back</Icon>
                </IconButton>
            </div>
            <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                Edit Account
            </Typography>
            <Button color="inherit">
                Save
            </Button>
        </Paper>
        <FormView entity="Account">
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
        </FormView>
    </div>
))
