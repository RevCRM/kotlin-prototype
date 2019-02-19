
import * as React from "react"

import { IFieldComponentProps } from "./props"
import { getGridWidthProps } from "../../Grid"
import { Grid, FormControl, FormControlLabel, Checkbox, FormHelperText } from "@material-ui/core"

export const CheckboxControl: React.StatelessComponent<IFieldComponentProps> = (props) => {

    const fieldId = props.field.name

    let errorText = ""
    props.errors.forEach((err) => {
        errorText += err.message + ". "
    })

    const value = !!props.value

    const control = (
        <FormControl>
            {!props.noLabel &&
                <FormControlLabel
                    control={
                        <Checkbox
                            id={fieldId}
                            checked={value}
                            onChange={(event) => props.onChange(event.target.checked)}
                            color="primary"
                        />
                    }
                    label={props.label}
                    disabled={props.disabled || props.readonly}
                />
            }
            {errorText &&
            <FormHelperText error style={{ marginTop: 0 }}>
                {errorText}
            </FormHelperText>}
        </FormControl>
    )

    if (props.grid) {
        const gridWidthProps = getGridWidthProps(props.grid)
        return (
            <Grid item {...gridWidthProps} style={props.style}>
                {control}
            </Grid>
        )
    }
    else {
        return control
    }

}
