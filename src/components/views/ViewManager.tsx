import * as React from 'react';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Select from 'material-ui/Select';
import { MenuItem } from 'material-ui/Menu';

export function ViewManager() {
    return (
        <div>
            <Toolbar>
                <Typography type="title" color="inherit">
                    RevCRM
                </Typography>
                <Select value={1}>
                    <MenuItem value={1}>List View</MenuItem>
                    <MenuItem value={2}>Map View</MenuItem>
                </Select>
            </Toolbar>
        </div>
    );
}
