
import * as React from "react"
import { IGridProps, Grid, GridItem } from "../Grid"
import { Paper, CardHeader, CardContent } from "@material-ui/core"

export interface IPanelProps extends IGridProps {
    title?: string
}

export const Panel: React.SFC<IPanelProps> = (props) => {
    return (
        <GridItem {...props} >
            <Paper elevation={0} square={true}>
                {props.title && <CardHeader title={props.title} />}
                <CardContent>
                    <Grid>
                        {props.children}
                    </Grid>
                </CardContent>
            </Paper>
        </GridItem>
    )
}
