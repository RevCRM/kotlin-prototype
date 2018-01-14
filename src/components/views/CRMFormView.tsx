
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Paper from 'material-ui/Paper';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { IViewManagerContext, ViewAction } from 'rev-forms-materialui';
import withStyles, { WithStyles } from 'material-ui/styles/withStyles';

export interface ICRMFormViewProps {
    model: string;
}

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

class CRMFormViewC extends React.Component<ICRMFormViewProps & WithStyles<keyof typeof styles>> {

    context: IViewManagerContext;
    static contextTypes = {
        viewContext: PropTypes.object
    };

    render() {
        console.log('FormView props', this.props);
        console.log('FormView context', this.context);

        const title = (this.context.viewContext.isNew() ? 'New ' : 'Edit ')
            + this.context.viewContext.modelMeta.name;

        return (
            <div>
                {/* <Button raised color="primary" style={{ marginBottom: 20 }} disabled={!this.context.viewContext.dirty}>
                    <Done style={{ marginRight: 10 }} />
                    Save
                </Button> */}
                <ViewAction label="Save" type="post" url="/todo" />
                <Paper className={this.props.classes.root}>
                    <Toolbar className={this.props.classes.toolbar}>
                        <Typography type="title" color="inherit">
                            {title}
                        </Typography>
                    </Toolbar>
                    <div className={this.props.classes.formWrapper}>
                        {this.props.children}
                    </div>
                </Paper>
            </div>
        );
    }
}

export const CRMFormView: React.ComponentType<ICRMFormViewProps> = withStyles(styles)(CRMFormViewC);
