
import * as React from "react"
import { Typography } from "@material-ui/core"

export interface IReadOnlyValueProps {
    hasLabel: boolean
}

export const ReadOnlyValue: React.SFC<IReadOnlyValueProps> = (props) => (
    <Typography style={{
        fontSize: 16, padding: "5px 0 7px",
        marginTop: props.hasLabel ? 16 : 0,
        whiteSpace: "pre-line"
    }}>
        {props.children}
    </Typography>
)
