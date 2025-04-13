import { PlatformType } from "@/api/model/platformModel";
import img1 from '@/assets/img/1.jpg'
export const PlatformList: PlatformType[] = [
    {
        'label': '必应搜索',
        'value': 'bingying',
        'logo': img1,
        'url': 'https://www.bing.com/search?q=',
        'hint': '微软必应搜索',
    },
    {
        'label': '谷歌学术',
        'value': 'google',
        'logo': img1,
        'url': 'https://scholar.google.com/scholar?hl=zh-CN&as_sdt=0%2C3&q=',
        'hint': '谷歌学术搜索(需要科学上网)',
    },
    {
        'label': '万方学术',
        'value': 'wanfang',
        'logo': img1,
        'url': 'https://s.wanfangdata.com.cn/paper?q=',
        'hint': '万方学术搜索',
    },
    {
        'label': '百度学术',
        'value': 'baidu',
        'logo': img1,
        'url': 'https://xueshu.baidu.com/s?wd=',
        'hint': '百度学术搜索',
    },
    {
        'label': 'X-MOL',
        'value': 'x-mol',
        'logo': img1,
        'url': 'https://www.x-mol.com/paper/search?q?option=',
        'hint': 'X-MOL学术搜索',
    },
    {
        'label': 'PubMed',
        'value': 'pubmed',
        'logo': img1,
        'url': 'https://pubmed.ncbi.nlm.nih.gov/?term=',
        'hint': 'PubMed Search',
    },
    {
        'label': 'Github',
        'value': 'github',
        'logo': img1,
        'url': 'https://github.com/search?q=',
        'hint': 'Github Search',
    },
    {
        'label': '专利检索',
        'value': 'patent',
        'logo': img1,
        'url': 'https://s.wanfangdata.com.cn/patent?q=',
        'hint': '专利检索',
    },
    {
        'label': '在线翻译',
        'value': 'translate',
        'logo': img1,
        'url': 'https://fanyi.baidu.com/mtpe-individual/multimodal?query=',
        'hint': '输入翻译内容',
    },
]