
import * as React from 'react';
import * as PropTypes from 'prop-types';

import Paper from 'material-ui/Paper';
import Toolbar from 'material-ui/Toolbar';
import Button from 'material-ui/Button';
import Done from 'material-ui-icons/Done';
import Typography from 'material-ui/Typography';
import { IViewContext } from './ViewManager';
import withStyles, { WithStyles } from 'material-ui/styles/withStyles';

export interface IFormViewProps {
    model: string;
}

const styles = {
    root: {
        width: '100%',
        overflowX: 'auto',
    },
    toolbar: {
        display: 'flex',
        marginBottom: 20
    }
};

class FormViewC extends React.Component<IFormViewProps & WithStyles<keyof typeof styles>> {

    context: { viewContext: IViewContext };
    static contextTypes = {
        viewContext: PropTypes.object
    };

    render() {
        console.log('FormView props', this.props);
        console.log('FormView context', this.context);

        const title = (this.context.viewContext.primaryKeyValue ? 'Edit ' : 'New ')
            + this.context.viewContext.modelMeta.name;

        return (
            <div>
                <Button raised color="primary" style={{ marginBottom: 20 }} disabled>
                    <Done style={{ marginRight: 10 }} />
                    Save
                </Button>
                <Paper className={this.props.classes.root}>
                    <Toolbar className={this.props.classes.toolbar}>
                        <Typography type="title" color="inherit">
                            {title}
                        </Typography>
                    </Toolbar>
                    <div>Form</div>
                </Paper>
            </div>
        );
    }
}

export const FormView: React.ComponentType<IFormViewProps> = withStyles(styles as any)(FormViewC);
