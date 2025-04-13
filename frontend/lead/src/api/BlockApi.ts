import axios from 'axios';
import {apiUrls}  from '@/api/api';

export async function fetchBlockData() {
    try {
        const response = await axios.get(apiUrls.Get_Blocks_API_URL);
        return response.data;
    } catch (err: unknown) {
        if (err instanceof Error) {
            console.error(`Data fetch error: ${err.message}`);
        } else {
            console.error(`Data fetch error: ${String(err)}`);
        }
    }
}
