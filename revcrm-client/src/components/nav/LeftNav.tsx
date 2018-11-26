import * as React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import DashboardIcon from '@material-ui/icons/InsertChart';
import CalendarIcon from '@material-ui/icons/InsertInvitation';
import ContactsIcon from '@material-ui/icons/SupervisedUserCircle';
import SalesIcon from '@material-ui/icons/MonetizationOnSharp';
import ServiceIcon from '@material-ui/icons/Assignment';
import SettingsIcon from '@material-ui/icons/Settings';
import LogOutIcon from '@material-ui/icons/PowerSettingsNew';
import { Divider, createStyles, withStyles, WithStyles } from '@material-ui/core';

const styles = createStyles({
    listItemText: {
        padding: 0,
        whiteSpace: 'nowrap'
    }
});

export const LeftNav = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <List>
        <ListItem button>
            <ListItemIcon><DashboardIcon /></ListItemIcon>
            <ListItemText
                primary="Dashboard"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <ListItem button>
            <ListItemIcon><CalendarIcon /></ListItemIcon>
            <ListItemText
                primary="Calendar &amp; Tasks"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <ListItem button>
            <ListItemIcon><ContactsIcon /></ListItemIcon>
            <ListItemText
                primary="Companies &amp; Contacts"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <ListItem button>
            <ListItemIcon><SalesIcon /></ListItemIcon>
            <ListItemText
                primary="Sales &amp; Marketing"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <ListItem button>
            <ListItemIcon><ServiceIcon /></ListItemIcon>
            <ListItemText
                primary="Customer Service"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <Divider />
        <ListItem button>
            <ListItemIcon><SettingsIcon /></ListItemIcon>
            <ListItemText
                primary="Settings"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <ListItem button>
            <ListItemIcon><LogOutIcon /></ListItemIcon>
            <ListItemText
                primary="Log Out"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
    </List>
));
