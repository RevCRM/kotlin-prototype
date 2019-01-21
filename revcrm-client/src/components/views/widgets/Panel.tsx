
import * as React from "react"
import { IGridProps, Grid, GridItem } from "../Grid"
import { CardHeader, CardContent, Card } from "@material-ui/core"

export interface IPanelProps extends IGridProps {
    title?: string
}

export const Panel: React.SFC<IPanelProps> = (props) => {
    return (
        <GridItem {...props} >
            <Card>
                {props.title && <CardHeader title={props.title} />}
                <CardContent>
                    <Grid>
                        {props.children}
                    </Grid>
                </CardContent>
            </Card>
        </GridItem>
    )
}
