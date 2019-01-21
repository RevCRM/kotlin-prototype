import { SelectionSetNode, FieldNode } from "graphql"

export function getFieldSelectionSet(fieldNames: string[]): SelectionSetNode {
    return {
        kind: "SelectionSet",
        selections: fieldNames.map(field => {
            const fieldNode: FieldNode = {
                kind: "Field",
                name: {
                    kind: "Name",
                    value: field
                }
            }
            return fieldNode
        })
    }
}
