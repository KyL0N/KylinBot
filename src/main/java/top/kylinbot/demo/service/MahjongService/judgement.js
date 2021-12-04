//向听判断
function main() {
	console.log(convert_to_card('2235m4088p3689s32z'));
}


function convert_to_card(hand_string)
{
	let card_numbers=[];
	let hand=JSON.parse(JSON.stringify(basic_hand));
	for(let i=0;i<hand_string.length;++i)
	{
		switch(hand_string[i])
		{
			case 'm':if(!put_card(card_numbers,hand[0],false))return basic_hand;break;
			case 'p':if(!put_card(card_numbers,hand[1],false))return basic_hand;break;
			case 's':if(!put_card(card_numbers,hand[2],false))return basic_hand;break;
			case 'z':if(!put_card(card_numbers,hand[3],true))return basic_hand;break;
			default:if(!put_number(card_numbers,hand_string[i]))return basic_hand;
		}
	}
	return hand;
}

function convert_to_hold(card)
{
	return mpsz.indexOf(card[1])*9+((card[0]=="0"&&card[1]!="z")?4:(card[0]-"1"));
}

function put_card(card_numbers,hand_color,worded)
{
	if(card_numbers.length==0)
		return false;
	while(card_numbers.length>0)
	{
		let the_number=card_numbers.pop();
		if(worded&&the_number>=7)
			return false;
		if(the_number==9)
			the_number=4;
		if(++hand_color[the_number]>4)
			return false;
	}
	return true;
}

function put_number(card_numbers,the_number)
{
	switch(the_number)
	{
		case '0':card_numbers.push(9);return true;
		case '1':card_numbers.push(0);return true;
		case '2':card_numbers.push(1);return true;
		case '3':card_numbers.push(2);return true;
		case '4':card_numbers.push(3);return true;
		case '5':card_numbers.push(4);return true;
		case '6':card_numbers.push(5);return true;
		case '7':card_numbers.push(6);return true;
		case '8':card_numbers.push(7);return true;
		case '9':card_numbers.push(8);return true;
		default:return false;
	}
}

function hand_amount(hand)
{
	let amount=0;
	for(let i=0;i<4;++i)
		for(let j=0;j<((i==3)?7:9);++j)
			amount+=hand[i][j];
	return amount;
}

function mahjong_distance(compared,color,hand)
{
	let distance=0;
	for(let i=0;i<9;++i)
		if(compared[i]>hand[color][i])
			distance+=compared[i]-hand[color][i];
	return distance;
}

function min_tenpai_distance(compared_type,color,hand)
{
	let distance=14;
	let possible_min_distance=0;
	for(let i=0;i<9;++i)
		possible_min_distance+=compared_type[0][i];
	for(let i=0;i<9;++i)
		possible_min_distance-=hand[color][i];
	for(let i=0;i<compared_type.length;++i)
	{
		if(distance==possible_min_distance)
			return possible_min_distance;
		let this_distance=mahjong_distance(compared_type[i],color,hand);
		if(this_distance<distance)
			distance=this_distance;
	}
	return distance;
}

function min_distance(color,type,hand)
{
	switch(type)
	{
		case 0:return 0;
		case 1:return min_tenpai_distance((color<3?mianzi_single_colored:mianzi_single_worded),color,hand);
		case 2:return min_tenpai_distance((color<3?mianzi_double_colored:mianzi_double_worded),color,hand);
		case 3:return min_tenpai_distance((color<3?mianzi_triple_colored:mianzi_triple_worded),color,hand);
		case 4:return min_tenpai_distance((color<3?mianzi_quadruple_colored:mianzi_quadruple_worded),color,hand);
	}
}

function min_distance_with_quetou(color,type,hand)
{
	switch(type)
	{
		case 0:return min_tenpai_distance((color<3?quetou_colored:quetou_worded),color,hand);
		case 1:return min_tenpai_distance((color<3?mianzi_single_colored_with_quetou:mianzi_single_worded_with_quetou),color,hand);
		case 2:return min_tenpai_distance((color<3?mianzi_double_colored_with_quetou:mianzi_double_worded_with_quetou),color,hand);
		case 3:return min_tenpai_distance((color<3?mianzi_triple_colored_with_quetou:mianzi_triple_worded_with_quetou),color,hand);
		case 4:return min_tenpai_distance((color<3?mianzi_quadruple_colored_with_quetou:mianzi_quadruple_worded_with_quetou),color,hand);
	}
}

function initialize_santen_U(hand)
{
	let U=[[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]];
	for(let i=0;i<4;++i)
		for(let j=0;j<5;++j)
			U[i][j]=min_distance(i,j,hand);
	return U;
}

function initialize_santen_T(hand)
{
	let T=[[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]];
	for(let i=0;i<4;++i)
		for(let j=0;j<5;++j)
			T[i][j]=min_distance_with_quetou(i,j,hand);
	return T;
}

function residue_from_hand(hand)
{
	let the_residue=[];
	for(let i=0;i<4;++i)
	{
		the_residue[i]=[];
		for(let j=0;j<((i==3)?7:9);++j)
			the_residue[i][j]=4-hand[i][j];
	}
	return the_residue;
}

function general_santen(hand,residue=residue_from_hand(hand))
{
	/*let santen_U=initialize_santen_U(hand);
	let santen_T=initialize_santen_T(hand);
	U[i][j]代表花色i里分配j个面子无雀头时的最小向听，T[i][j]代表花色i里分配j个面子无雀头时的最小向听*/
	let santen_U,santen_T;
	[santen_U,santen_T]=initialize_santen(hand,residue);
	/*u[i][j]代表前i个花色里分配j个面子无雀头时的最小向听，t[i][j]代表前i个花色里分配j个面子无雀头时的最小向听*/
	let santen_u=[[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]];
	let santen_t=[[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]];
	for(let j=0;j<5;++j)
	{
		santen_u[0][j]=santen_U[0][j];
		santen_t[0][j]=santen_T[0][j];
	}
	//使用动态规划从部分向听计算整体向听。
	for(let i=1;i<4;++i)
		for(let j=0;j<5;++j)
		{
			let count_u=14;
			let count_t=14;
			for(let l=0;l<=j;++l)
			{
				if(count_u>santen_u[i-1][l]+santen_U[i][j-l])
					count_u=santen_u[i-1][l]+santen_U[i][j-l];
				if(count_t>santen_t[i-1][l]+santen_U[i][j-l])
					count_t=santen_t[i-1][l]+santen_U[i][j-l];
				if(count_t>santen_u[i-1][l]+santen_T[i][j-l])
					count_t=santen_u[i-1][l]+santen_T[i][j-l];
			}
			santen_u[i][j]=count_u;
			santen_t[i][j]=count_t;
		}
	switch(hand_amount(hand))
	{
		case 1:return 1;
		case 2:return santen_t[3][0];
		case 4:return santen_t[3][1];
		case 5:return santen_t[3][1];
		case 7:return santen_t[3][2];
		case 8:return santen_t[3][2];
		case 10:return santen_t[3][3];
		case 11:return santen_t[3][3];
		case 13:return santen_t[3][4];
		case 14:return santen_t[3][4];
		default:return Infinity;
	}
}

function qidui_santen(hand)
{
	let duizishu=0;
	let paizhongshu=0;
	for(let i=0;i<4;++i)
		for(let j=0;j<9;++j)
		{
			if(hand[i][j]>=2)
				++duizishu;
			if(hand[i][j]>=1)
				++paizhongshu;
		}
	if(paizhongshu>7)
		paizhongshu=7;
	return 14-paizhongshu-duizishu;
}

function kuosi_santen(hand)
{
	let yaojiuzhongshu=0;
	let yaojiushu=0;
	for(let i=0;i<3;++i)
	{
		yaojiushu+=hand[i][0];
		if(hand[i][0]>=1)
			++yaojiuzhongshu;
		yaojiushu+=hand[i][8];
		if(hand[i][8]>=1)
			++yaojiuzhongshu;
	}
	for(let j=0;j<7;++j)
	{
		yaojiushu+=hand[3][j];
		if(hand[3][j]>=1)
			++yaojiuzhongshu;
	}
	return ((yaojiushu>yaojiuzhongshu)?13:14)-yaojiuzhongshu;
}

function santen(hand,residue)
{
	return min([(hand_amount(hand)>=13?kuosi_santen(hand):Infinity),(hand_amount(hand)>=13?qidui_santen(hand):Infinity),general_santen(hand,residue)]);
}

function santen_reply(hand,residue)
{
	if(hand_amount(hand)==0)
		return "牌型有问题呢……";
	let hand_santen=santen(hand,residue);
	switch(hand_santen)
	{
		case 0: return "和了呢~";
		case 1: return "听牌~";
		default: return (hand_santen-1)+"向听~";
	}
}