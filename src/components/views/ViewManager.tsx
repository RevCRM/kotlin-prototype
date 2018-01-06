import * as React from 'react';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Select from 'material-ui/Select';
import { MenuItem } from 'material-ui/Menu';
import { withStyles } from 'material-ui/styles';
import { WithStyles } from 'material-ui/styles/withStyles';

const styles = {
    root: {
        marginTop: 70
    },
    viewList: {
        marginLeft: 30
    }
};

function ViewManagerC(props: WithStyles<any>) {
    return (
        <div className={props.classes.root}>
            <Toolbar>
                <Typography type="title" color="inherit">
                    All Companies
                </Typography>
                <Select value={1} className={props.classes.viewList}>
                    <MenuItem value={1}>List View</MenuItem>
                    <MenuItem value={2}>Map View</MenuItem>
                </Select>
            </Toolbar>
        </div>
    );
}

export const ViewManager = withStyles(styles)(ViewManagerC);
