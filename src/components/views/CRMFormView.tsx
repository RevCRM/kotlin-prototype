
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Paper from 'material-ui/Paper';
import Grid from 'material-ui/Grid';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { IModelContextProp, ViewAction } from 'rev-ui';
import withStyles, { WithStyles } from 'material-ui/styles/withStyles';
import { ICRMViewManagerContext } from './CRMViewManager';
import { withModelContext } from 'rev-ui/lib/views/withModelContext';
import { DetailView } from 'rev-ui/lib/views/DetailView';

const styles = {
    root: {
        width: '100%'
    },
    toolbar: {
        display: 'flex',
        borderBottom: '1px solid #EBEBEB'
    },
    formWrapper: {
        padding: '10 20'
    }
};

class CRMFormToolbarC extends React.Component<IModelContextProp & WithStyles<keyof typeof styles>> {

    render() {
        const { manager, model, modelMeta } = this.props.modelContext;

        const title = !model ? 'Loading...'
            : (manager.isNew(model) ? 'New ' : 'Edit ') + modelMeta.name;

        return (
            <Toolbar className={this.props.classes.toolbar}>
                <Typography type="title" color="inherit">
                    {title}
                </Typography>
            </Toolbar>
        );
    }

}

const CRMFormToolbar = withModelContext(withStyles(styles)(CRMFormToolbarC));

class CRMFormViewC extends React.Component<WithStyles<keyof typeof styles>> {

    context: ICRMViewManagerContext;
    static contextTypes = {
        viewContext: PropTypes.object
    };

    render() {
        console.log('FormView props', this.props);
        console.log('FormView context', this.context);

        const ctx = this.context.viewContext;

        return (
            <DetailView model={ctx.view.model} primaryKeyValue={ctx.primaryKeyValue}>
                {/* <Button raised color="primary" style={{ marginBottom: 20 }} disabled={!this.context.viewContext.dirty}>
                    <Done style={{ marginRight: 10 }} />
                    Save
                </Button> */}
                <ViewAction label="Save" type="post" url="/todo" />
                <Paper className={this.props.classes.root}>
                    <CRMFormToolbar />
                    <Grid container spacing={8} className={this.props.classes.formWrapper}>
                        {this.props.children}
                    </Grid>
                </Paper>
            </DetailView>
        );
    }
}

export const CRMFormView: React.ComponentType = withStyles(styles)(CRMFormViewC);
