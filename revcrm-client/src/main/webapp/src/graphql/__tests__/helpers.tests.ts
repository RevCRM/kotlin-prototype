import { getSelectionSet } from "../helpers"
import { FieldNode } from "graphql"

describe("getSelectionSet()", () => {

    it("returns a flat SelectionSet with selected fields", () => {
        const selections = {
            id: true,
            name: true
        }
        const ast = getSelectionSet(selections)
        expect(ast).toHaveProperty("kind", "SelectionSet")
        expect(ast).toHaveProperty("selections")
        expect(ast.selections).toHaveLength(2)
        const sels = ast.selections as FieldNode[]
        expect(sels[0].name.value).toEqual("id")
        expect(sels[1].name.value).toEqual("name")
    })

    it("returns a nested SelectionSet with selected fields", () => {
        const selections = {
            id: true,
            name: true,
            nested: {
                value: true,
                test: {
                    detail: true
                }
            }
        }
        const ast = getSelectionSet(selections)
        expect(ast.selections).toHaveLength(3)

        const sels = ast.selections as FieldNode[]
        expect(sels[0].name.value).toEqual("id")
        expect(sels[1].name.value).toEqual("name")
        expect(sels[2].name.value).toEqual("nested")
        expect(sels[2].selectionSet).not.toBeNull()

        const nestedFields = sels[2].selectionSet!.selections as FieldNode[]
        expect(nestedFields).toHaveLength(2)
        expect(nestedFields[0].name.value).toEqual("value")
        expect(nestedFields[1].name.value).toEqual("test")

        const testFields = nestedFields[1].selectionSet!.selections as FieldNode[]
        expect(testFields).toHaveLength(1)
        expect(testFields[0].name.value).toEqual("detail")

    })

})
