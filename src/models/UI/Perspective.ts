
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api';
import { View } from './View';

@ApiOperations(['read'])
export class Perspective {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        label: string;
    @rev.TextField()
        model: string;
    @rev.RecordListField({ model: 'View' })
        views: View[];

}
