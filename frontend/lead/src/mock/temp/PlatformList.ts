import type { PlatformType } from '@/api/model/platformModel'
import logo0 from '@/assets/svg/biying.svg';
import logo1 from '@/assets/svg/Google.svg';
import logo2 from '@/assets/svg/wanfang.svg';
import logo3 from '@/assets/svg/baidu.svg';
import logo4 from '@/assets/svg/xmol.svg';
import logo5 from '@/assets/svg/pubmed.svg';
import logo6 from '@/assets/svg/GitHub.svg'
import logo7 from '@/assets/svg/apply.svg';
import logo8 from '@/assets/svg/translate.svg';


export const PlatformList: PlatformType[] = [
  {
    label: '必应搜索',
    value: 'bing',
    logo: logo0,
    url: 'https://www.bing.com/search?q=',
    hint: '微软必应搜索',
  },
  {
    label: '谷歌学术',
    value: 'google',
    logo: logo1,
    url: 'https://scholar.google.com/scholar?hl=zh-CN&as_sdt=0%2C3&q=',
    hint: '谷歌学术搜索(需要科学上网)',
  },
  {
    label: '万方学术',
    value: 'wanfang',
    logo: logo2,
    url: 'https://s.wanfangdata.com.cn/paper?q=',
    hint: '万方学术搜索',
  },
  {
    label: '百度学术',
    value: 'baidu',
    logo: logo3,
    url: 'https://xueshu.baidu.com/s?wd=',
    hint: '百度学术搜索',
  },
  {
    label: 'X-MOL',
    value: 'x-mol',
    logo: logo4,
    url: 'https://www.x-mol.com/paper/search?q?option=',
    hint: 'X-MOL学术搜索',
  },
  {
    label: 'PubMed',
    value: 'pubmed',
    logo: logo5,
    url: 'https://pubmed.ncbi.nlm.nih.gov/?term=',
    hint: 'PubMed Search',
  },
  {
    label: 'Github',
    value: 'github',
    logo: logo6,
    url: 'https://github.com/search?q=',
    hint: 'Github Search',
  },
  {
    label: '专利检索',
    value: 'patent',
    logo: logo7,
    url: 'https://s.wanfangdata.com.cn/patent?q=',
    hint: '专利检索',
  },
  {
    label: '在线翻译',
    value: 'translate',
    logo: logo8,
    url: 'https://fanyi.baidu.com/mtpe-individual/multimodal?query=',
    hint: '输入翻译内容',
  },
];