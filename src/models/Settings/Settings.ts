
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api';
import { Perspective } from '../UI/Perspective';
import { RevCRMModel } from '../RevCRMModel';

@ApiOperations(['read'])
export class Settings extends RevCRMModel<Settings> {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.RelatedModel({ model: 'Perspective' })
        defaultPerspective: Perspective;

}
