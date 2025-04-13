import type { transformTextType } from '@/api/model/transformTextModel'
import axios from 'axios'
import {apiUrls}  from '@/api/api';

export async function fetchBlockData(transformText:transformTextType) {
    try {
        const response = await axios.get(apiUrls.UploadFiletransform_API_URL);
        return response.data;
    } catch (err: unknown) {
        if (err instanceof Error) {
            console.error(`Data fetch error: ${err.message}`);
        } else {
            console.error(`Data fetch error: ${String(err)}`);
        }
    }
}
