import * as React from "react"
import { IGridProps, GridItem } from "../views/Grid"
import { FormControl, InputLabel, Input, FormHelperText } from "@material-ui/core"

export interface IFieldProps extends IGridProps {
    name: string
    label?: string
}

export const Field = (props: IFieldProps) => {
    const controlId = `field_${props.name}`
    const error = false
    const disabled = false
    const errorText = ""
    return (
        <GridItem {...props} >
            <FormControl fullWidth>
                <InputLabel
                    htmlFor={controlId}
                    error={error}
                    disabled={disabled}
                >
                    {props.label || props.name}
                </InputLabel>
                <Input
                    id={controlId}
                    // value={props.value || ''}
                    // onChange={(event) => props.onChange(event.target.value)}
                    error={error}
                    disabled={disabled}
                />
                {errorText &&
                    <FormHelperText error>
                        {errorText}
                    </FormHelperText>}
            </FormControl>
        </GridItem>
    )
}
