import * as React from 'react';

import { Toolbar, ToolbarGroup, ToolbarTitle } from 'material-ui/Toolbar';
import DropDownMenu from 'material-ui/DropDownMenu';
import MenuItem from 'material-ui/MenuItem';

export function ViewManager() {
    return (
        <div>
            <Toolbar>
                <ToolbarGroup>
                    <ToolbarTitle text="My Accounts" />
                    <DropDownMenu value={1}>
                        <MenuItem value={1} primaryText="List View" />
                        <MenuItem value={2} primaryText="Map View" />
                    </DropDownMenu>
                </ToolbarGroup>
            </Toolbar>
        </div>
    );
}
