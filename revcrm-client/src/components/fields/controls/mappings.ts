import { TextControl } from "./TextControl"
import { IFieldMetadata } from "../../meta/Metadata"
import { IFieldComponentProps } from "./props"

export function getFieldControlMapping(field: IFieldMetadata) {

    const mappings: {
        [fieldType: string]: React.ComponentType<IFieldComponentProps>
    } = {
        IntegerField: TextControl,
        FloatField: TextControl,
        DecimalField: TextControl,
        BooleanField: TextControl,
        TextField: TextControl,
        DateField: TextControl,
        TimeField: TextControl,
        DateTimeField: TextControl,
        EnumField: TextControl,
        RelatedEntityField: TextControl,
    }

    return mappings[field.type]
}
