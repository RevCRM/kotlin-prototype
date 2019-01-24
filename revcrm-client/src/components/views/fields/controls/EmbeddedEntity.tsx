
import * as React from "react"
import { IFieldComponentProps } from "./props"
import { FormContext, IFormContext } from "../../FormView"
import { IEntityMetadata, IFieldMetadata } from "../../../meta/Metadata"
import { Grid } from "@material-ui/core"
import { getGridWidthProps } from "../../Grid"

export const EmbeddedEntityControl =
    class extends React.Component<IFieldComponentProps> {
    entity: string
    entityMeta: IEntityMetadata
    embeddedEntityData: any

    constructor(props: any) {
        super(props)
        this.entity = this.props.field.constraints["Entity"]
        // TODO: This should not assume getEntity() returns an entity
        this.entityMeta = this.props.meta.getEntity(this.entity)!
        this.embeddedEntityData = this.props.value || {}
    }

    onFieldChange = (field: IFieldMetadata, value: any) => {
        Object.assign(this.embeddedEntityData, { [field.name]: value })
        this.props.onChange(this.embeddedEntityData)
    }

    render() {

        const { loadState, mode } = this.props.form
        const dirtyFields: string[] = [] // TODO
        const gridWidthProps = getGridWidthProps(this.props)

        const formContext: IFormContext = {
            loadState,
            entity: this.entity,
            mode,
            entityData: this.embeddedEntityData,
            dirtyFields,
            onFieldChange: this.onFieldChange,
            save: this.props.form.save
        }

        return (
            <FormContext.Provider value={formContext}>
                <Grid item {...gridWidthProps} container spacing={16}>
                    {this.props.children}
                </Grid>
            </FormContext.Provider>
        )
    }
}
