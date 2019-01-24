import * as React from "react"
import { IGridProps, Grid, GridItem } from "../Grid"
import { CardHeader, CardContent, Card, Theme, createStyles, WithStyles, withStyles } from "@material-ui/core"

export const styles = (theme: Theme) => createStyles({
    cardRoot: {
        overflow: "visible"
    }
})

export interface IPanelProps extends
        IGridProps,
        WithStyles<typeof styles> {
    title?: string
    children?: any
}

export const Panel = withStyles(styles)((props: IPanelProps) => {
    return (
        <GridItem {...props}>
            <Card classes={{ root: props.classes.cardRoot }}>
                {props.title && <CardHeader title={props.title} />}
                <CardContent>
                    <Grid>
                        {props.children}
                    </Grid>
                </CardContent>
            </Card>
        </GridItem>
    )
})
