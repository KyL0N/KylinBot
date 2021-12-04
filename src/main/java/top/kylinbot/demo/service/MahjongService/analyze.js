//牌理分析
function paili_13(hand)
{
	let add_choice=JSON.parse(JSON.stringify(basic_hand));
	for(let i=0;i<4;++i)
		for(let j=0;j<(i==3?7:9);++j)
		{
			if(hand[i][j]==4)
				continue;
			let draw_test=JSON.parse(JSON.stringify(hand));
			++draw_test[i][j];
			if(santen(draw_test)<santen(hand))
				add_choice[i][j]+=4-hand[i][j];
		}
	return add_choice;
}

function paili_14(hand)
{
	let minus_choice=[];
	for(let i=0;i<4;++i)
		for(let j=0;j<(i==3?7:9);++j)
		{
			if(hand[i][j]==0)
				continue;
			let draw_test=JSON.parse(JSON.stringify(hand));
			--draw_test[i][j];
			if(santen(draw_test)<=santen(hand))
				minus_choice.push(i*10+j);
		}
	return minus_choice;
}

function paili(hand)
{
	let try_hand=JSON.parse(JSON.stringify(hand));
	if(hand_amount(hand)%3==0)
		return "";
	if(hand_amount(hand)%3==1)
	{
		let add_choice=paili_13(hand);
		return "摸"+what_cards(add_choice)+" —— 共"+hand_amount(add_choice)+"枚";
	}
	if(hand_amount(hand)%3==2)
	{
		let minus_choice=paili_14(hand);
		let sort_minus_choice=[];
		let paili_string=convert_to_handcard(hand)+"的牌理:\n";
		for(let i=0;i<minus_choice.length;++i)
		{
			let qiechu_hand=JSON.parse(JSON.stringify(hand));
			--qiechu_hand[Math.floor(minus_choice[i]/10)][minus_choice[i]%10];
			let add_choice=paili_13(qiechu_hand);
			let add_amount=hand_amount(add_choice);
			sort_minus_choice[i]=[add_amount*100+(99-minus_choice[i]),Math.floor(minus_choice[i]/10),minus_choice[i]%10,"摸"+what_cards(add_choice)+" —— 共"+add_amount+"枚"];
		}
		sort_minus_choice.sort((a,b)=>(b[0]-a[0]));
		for(let i=0;i<sort_minus_choice.length;++i)
			paili_string+="切"+(sort_minus_choice[i][2]+1)+mpsz[sort_minus_choice[i][1]]+", "+sort_minus_choice[i][3]+(i==minus_choice.length-1?".":";\n");
		return paili_string;
	}
}

function convert_to_handcard(hand)
{
	let card_string="";
	for(let i=0;i<4;++i)
	{
		let card_length=card_string.length;
		for(let j=0;j<(i==3?7:9);++j)
			for(let k=0;k<hand[i][j];++k)
				card_string+=(j+1);
		if(card_string.length>card_length)
			card_string+=mpsz[i];
	}
	return card_string;
}

function what_cards(hand)
{
	let card_string="";
	for(let i=0;i<4;++i)
	{
		let card_length=card_string.length;
		for(let j=0;j<(i==3?7:9);++j)
			if(hand[i][j]>0)
				card_string+=(j+1);
		if(card_string.length>card_length)
			card_string+=mpsz[i];
	}
	return card_string;
}