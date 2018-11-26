import * as React from 'react';

import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import Typography from '@material-ui/core/Typography';
import { withStyles, WithStyles, createStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/Inbox';

export const styles = createStyles({
    root: {
        width: '100%',
    },
    appBar: {
        '@media print': {
            display: 'none'
        }
    },
    flex: {
        flex: 1,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
    mainBody: {
        marginTop: 70
    }
});

export interface INavigationState {
    leftNavOpen: boolean;
}

export const Navigation = withStyles(styles)(
    class extends React.Component<WithStyles<keyof typeof styles>, INavigationState> {

    constructor(props: any) {
        super(props);
        this.state = {
            leftNavOpen: false
        };
    }

    onClickMenuButton = () => {
        this.setState((prevState) => ({
            leftNavOpen: !prevState.leftNavOpen
        }));
    }

    render() {
        return (
            <div className={this.props.classes.root}>

                <AppBar position="fixed" className={this.props.classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" aria-label="Menu" className={this.props.classes.menuButton}
                            onClick={this.onClickMenuButton}>
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="h6" color="inherit" className={this.props.classes.flex}>
                            RevCRM
                        </Typography>
                    </Toolbar>
                </AppBar>

                <Drawer open={this.state.leftNavOpen} onClose={this.onClickMenuButton}>
                    <List>
                        <ListItem button>
                            <ListItemIcon><InboxIcon /></ListItemIcon>
                            <ListItemText primary="Menu Item 1" />
                        </ListItem>
                        <ListItem button>
                            <ListItemIcon><InboxIcon /></ListItemIcon>
                            <ListItemText primary="Menu Item 2" />
                        </ListItem>
                    </List>
                </Drawer>

                <div className={this.props.classes.mainBody}>
                    {this.props.children}
                </div>

            </div>
        );
    }

});
