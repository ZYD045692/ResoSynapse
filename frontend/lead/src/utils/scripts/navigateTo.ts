import { apiUrls} from '@/api/api';
import axios from 'axios';


function scrollToTopPage(moduleId:number) {
    const targetElement = document.getElementById(`section-${moduleId}`);
    targetElement?.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
    });
}


export { scrollToTopPage };
