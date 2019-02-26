
import * as React from "react"

import { IFieldComponentProps } from "./props"
import { getGridWidthProps } from "../../../views/Grid"
import { Grid, FormControl, InputLabel, Input, FormHelperText } from "@material-ui/core"
import { ReadOnlyValue } from "./ReadOnlyValue"

export const TextControl: React.StatelessComponent<IFieldComponentProps> = (props) => {

    const fieldId = props.field.name
    const { MultiLine } = props.field.properties

    const hasLabel = props.label !== false
    const hasErrors = props.errors.length > 0
    let errorText = ""
    props.errors.forEach((err) => {
        errorText += err.message + ". "
    })

    const style = {
        minHeight: 64,
        ...props.style
    }

    const control = (
        <FormControl fullWidth>
            {hasLabel &&
                <InputLabel
                    htmlFor={fieldId}
                    error={hasErrors}
                    shrink={true}
                >
                    {props.label}
                </InputLabel>
            }
            {!props.readonly &&
            <Input
                id={fieldId}
                type="text"
                value={props.value || ""}
                onChange={(event) => props.onChange(event.target.value)}
                error={hasErrors}
                disabled={props.disabled}
                multiline={MultiLine == "true"}
            />}
            {props.readonly &&
            <ReadOnlyValue hasLabel={hasLabel}>{props.value || ""}</ReadOnlyValue>}
            {errorText &&
            <FormHelperText error>
                {errorText}
            </FormHelperText>}
        </FormControl>
    )

    if (props.grid) {
        const gridWidthProps = getGridWidthProps(props.grid)
        return (
            <Grid item {...gridWidthProps} style={style}>
                {control}
            </Grid>
        )
    }
    else {
        return control
    }
}
