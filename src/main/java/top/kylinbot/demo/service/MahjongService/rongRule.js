//和牌牌理
function convert_to_hold(card)
{
	return mpsz.indexOf(card[1])*9+((card[0]=="0"&&card[1]!="z")?4:(card[0]-"1"));
}

function is_card(card_name)
{
	if(card_name.length!=2)
		return false;
	if(mpsz.indexOf(card_name[1])<0)
		return false;
	if(card_name[0]<"1")
		return false;
	if(card_name[0]>((card_name[1]=="z")?"7":"9"))
		return false;
	return true;
}

function is_red_dora(card_name)
{
	return card_name=="0m"||card_name=="0p"||card_name=="0s";
}

function paili_input(order_string)
{
	let hand,hold,dora=0,red_dora=0,li_dora=0,bei_dora=0,fulu=[],ming_kong=[],an_kong=[],tsumo=false,seat=7,ground_wind=7,reached=false,wreached=false,yifa=false,last_turn=false,linshang=false,qiangkong=false,first_turn=false;
	let order_section=[""],removed_section=[""],section_itt=0,remove_itt=false;
	for(let i=0;i<order_string.length;++i)
		if((order_string[i]==" ")||(order_string[i]=="+"))
		{
			remove_itt=false;
			order_section[++section_itt]="";
		}
		else if(order_string[i]=="-")
		{
			remove_itt=removed_section.length;
			removed_section[remove_itt]="";
		}
		else
		{
			if(remove_itt)
				removed_section[remove_itt]+=order_string[i];
			else
				order_section[section_itt]+=order_string[i];
		}
	let have_hold=((order_section.length<2)?false:(is_card(order_section[1])||is_red_dora(order_section[1])));
	if(order_section.length<2||!have_hold)
	{
		if(order_section[0].length<2)
			return "输入有问题呢……";
		if(order_section[0].length==2)
			hand=basic_hand;
		else if(mpsz.indexOf(order_section[0][order_section[0].length-3])<0)
		{
			hand=convert_to_card(order_section[0].substr(0,order_section[0].length-2)+order_section[0][order_section[0].length-1]);
			if(hand_amount(hand)==0)
				return "输入有问题呢……";
		}
		else
		{
			hand=convert_to_card(order_section[0].substr(0,order_section[0].length-2));
			if(hand_amount(hand)==0)
				return "输入有问题呢……";
		}
		if(order_section[0].substr(order_section[0].length-2)=="0z")
			return order_string+"的牌理:\n"+"牌型有问题呢……";
		hold=convert_to_hold(order_section[0].substr(order_section[0].length-2));
		tsumo=true;
		red_dora+=substr_amount(order_section[0],"0");
	}
	else
	{
		hand=convert_to_card(order_section[0]);
		if(hand_amount(hand)==0)
			return "输入有问题呢……";
		if(order_section[1]=="0z")
			return order_string+"的牌理:\n"+"牌型有问题呢……";
		hold=convert_to_hold(order_section[1]);
		red_dora+=substr_amount(order_section[0],"0")+substr_amount(order_section[1],"0");
	}
	for(let i=0;i<removed_section.length;++i)
	{
		while(i!=0&&removed_section[i]=="")
			removed_section.splice(i,1);
		removed_section[i]=convert_to_card(removed_section[i]);
		if(i!=0&&hand_amount(removed_section[i])==0)
			return "输入牌河有问题呢……";
	}
	for(let i=0;i<4;++i)
		for(let j=0;j<((i==3)?7:9);++j)
			for(let k=1;k<removed_section.length;++k)
				removed_section[0][i][j]+=removed_section[k][i][j];
	for(let i=(have_hold?2:1);i<order_section.length;++i)
	{
		if((order_section[i][0]<"0"||order_section[i][0]>"9")&&order_section[i][0]!="#"&&order_section[i][0]!="口")
		{
			for(let j=0;j<order_section[i].length;++j)
				switch(order_section[i][j])
				{
					case "東":
					case "东": if(seat==0||seat==7)seat=0;else return "条件有问题呢~"; break;
					case "南": if(seat==1||seat==7)seat=1;else return "条件有问题呢~"; break;
					case "西": if(seat==2||seat==7)seat=2;else return "条件有问题呢~"; break;
					case "北": if(seat==3||seat==7)seat=3;else return "条件有问题呢~"; break;
					case "E":
					case "e": if(ground_wind==0||ground_wind==7)ground_wind=0;else return "条件有问题呢~"; break;
					case "S":
					case "s": if(ground_wind==1||ground_wind==7)ground_wind=1;else return "条件有问题呢~"; break;
					case "W":
					case "w": if(ground_wind==2||ground_wind==7)ground_wind=2;else return "条件有问题呢~"; break;
					case "N":
					case "n": if(ground_wind==3||ground_wind==7)ground_wind=3;else return "条件有问题呢~"; break;
					case "立":
					case "L":
					case "l":if(!reached&&!wreached&&!first_turn)reached=true;else return "条件有问题呢~"; break;
					case "双":
					case "雙":
					case "两":
					case "兩":
					case "R":
					case "r":if(!reached&&!wreached&&!first_turn)wreached=true;else return "条件有问题呢~"; break;
					case "一":
					case "发":
					case "発":
					case "Y":
					case "y":if(!first_turn&&!(last_turn&&!tsumo)&&!linshang)yifa=true;else return "条件有问题呢~"; break;
					case "海":if(!first_turn&&!last_turn&&!linshang){tsumo=true;last_turn=true;}else return "条件有问题呢~";break;
					case "河":if(tsumo)tsumo=false;
					case "H":
					case "h":if(!first_turn&&!last_turn&&!linshang)last_turn=true;else return "条件有问题呢~"; break;
					case "岭":
					case "嶺":if(!first_turn&&!last_turn&&!linshang&&!qiangkong&&!yifa){tsumo=true;linshang=true;} else return "条件有问题呢~"; break;
					case "枪":
					case "槍":if(!first_turn&&!last_turn&&!linshang&&!qiangkong){tsumo=false;linshang=true;} else return "条件有问题呢~"; break;
					case "K":
					case "k":if(!first_turn&&!last_turn&&!linshang&&!qiangkong){if(tsumo&&!yifa) linshang=true; else if(tsumo&&yifa) return "条件有问题呢~"; else qiangkong=true;} else "条件有问题呢~"; break;
					case "天":if((seat!=0)&&(seat!=7)) return "条件有问题呢~"; else seat=0; if(!reached&&!wreached&&!yifa&&!first_turn&&!last_turn&&!linshang&&!qiangkong){tsumo=true;first_turn=true;} else return "条件有问题呢~"; break;
					case "地":if(seat==0) return "条件有问题呢~";
					case "T":
					case "t":if(!reached&&!wreached&&!yifa&&!first_turn&&!last_turn&&!linshang&&!qiangkong){tsumo=true;first_turn=true;} else return "条件有问题呢~"; break;
					case "摸":
					case "M":
					case "m":if(!qiangkong) tsumo=true; else return "条件有问题呢~"; break;
					case "宝":
					case "寶":
					case "D":
					case "d":dora+=1;break;
					case "拔":
					case "B":
					case "b":bei_dora+=1;break;
					case "里":
					case "裏":li_dora+=1;break;
					default: return order_section[i]+"输入有问题呢~(条件输入格式不对)";
				}
		}
		else
		{
			switch(order_section[i].length)
			{
				case 5:if((order_section[i][0]=="口"||order_section[i][0]=="#")&&(order_section[i][3]=="口"||order_section[i][3]=="#")&&(order_section[i][1]==order_section[i][2])&&(order_section[i][1]!="0"))
					{
						let kong_card=order_section[i][1]+order_section[i][4];
						if(!is_card(kong_card))
							return order_section[i]+"输入有问题呢~(开的杠不是牌)";
						else
							an_kong.push(convert_to_hold(kong_card));
					}
					else if((order_section[i][1]=="口"||order_section[i][1]=="#")&&(order_section[i][2]=="口"||order_section[i][2]=="#")&&(order_section[i][0]==order_section[i][3])&&(order_section[i][0]!="0"))
					{
						let kong_card=order_section[i][0]+order_section[i][4];
						if(!is_card(kong_card))
							return order_section[i]+"输入有问题呢~(开的杠不是牌)";
						else
							an_kong.push(convert_to_hold(kong_card));
					}
					else if((order_section[i][0]==order_section[i][1])&&(order_section[i][1]==order_section[i][2])&&(order_section[i][2]==order_section[i][3])&&(order_section[i][0]!="0"))
					{
						let kong_card=order_section[i][0]+order_section[i][4];
						if(!is_card(kong_card))
							return order_section[i]+"输入有问题呢~(开的杠不是牌)";
						else
							ming_kong.push(convert_to_hold(kong_card));
					}
					else
					{
						if(order_section[i][4]=="z")
							return order_section[i]+"输入有问题呢~(开杠格式不对)";
						switch(order_section.substr(0,4))
						{
							case "口50口":
							case "口05口":
							case "#50#":
							case "#05#":
							case "5口口0":
							case "0口口5":
							case "5##0":
							case "0##5":red_dora+=1;an_kong.push(convert_to_hold(5+order_section[i][4]));break;
							case "口00口":
							case "0口口0":
							case "#00#":
							case "0##0":red_dora+=2;an_kong.push(convert_to_hold(5+order_section[i][4]));break;
							case "5550":
							case "5505":
							case "5055":
							case "0555":red_dora+=1;ming_kong.push(convert_to_hold(5+order_section[i][4]));break;
							case "5500":
							case "5005":
							case "0055":
							case "0505":
							case "0550":
							case "5050":red_dora+=2;ming_kong.push(convert_to_hold(5+order_section[i][4]));break;
							case "5000":
							case "0500":
							case "0050":
							case "0005":red_dora+=3;ming_kong.push(convert_to_hold(5+order_section[i][4]));break;
							case "0000":red_dora+=4;ming_kong.push(convert_to_hold(5+order_section[i][4]));break;
							default:return order_section[i]+"输入有问题呢~(开杠格式不对)";
						}
					}
					break;
				case 4:if(mpsz.indexOf(order_section[i][3])<0)
						return order_section[i]+"输入有问题呢~(没有这种花色)";
					if((order_section[i][3]=="z")&&((order_section[i][0]!=order_section[i][1])||(order_section[i][1]!=order_section[i][2])))
						return order_section[i]+"输入有问题呢~(字牌必须是刻子且没有红宝)";
					if((order_section[i][0]==order_section[i][1])&&(order_section[i][1]==order_section[i][2])&&(order_section[i][0]!="0"))
							fulu.push(order_section[i]);
					else
					{
						switch(order_section[i].substr(0,3))
						{
							case "123":
							case "213":
							case "312":
							case "132":
							case "231":
							case "321":fulu.push("123"+order_section[i][3]);break;
							case "234":
							case "324":
							case "423":
							case "243":
							case "342":
							case "432":fulu.push("234"+order_section[i][3]);break;
							case "340":
							case "430":
							case "034":
							case "304":
							case "403":
							case "043":red_dora+=1;
							case "345":
							case "435":
							case "534":
							case "354":
							case "453":
							case "543":fulu.push("345"+order_section[i][3]);break;
							case "406":
							case "046":
							case "640":
							case "460":
							case "064":
							case "604":red_dora+=1;
							case "456":
							case "546":
							case "645":
							case "465":
							case "564":
							case "654":fulu.push("456"+order_section[i][3]);break;
							case "067":
							case "607":
							case "706":
							case "076":
							case "670":
							case "760":red_dora+=1;
							case "567":
							case "657":
							case "756":
							case "576":
							case "675":
							case "765":fulu.push("567"+order_section[i][3]);break;
							case "678":
							case "768":
							case "867":
							case "687":
							case "786":
							case "876":fulu.push("678"+order_section[i][3]);break;
							case "789":
							case "879":
							case "978":
							case "798":
							case "897":
							case "987":fulu.push("789"+order_section[i][3]);break;
							case "055":
							case "505":
							case "550":red_dora+=1;fulu.push("555"+order_section[i][3]);break;
							case "005":
							case "500":
							case "050":red_dora+=2;fulu.push("555"+order_section[i][3]);break;
							case "000":red_dora+=3;fulu.push("555"+order_section[i][3]);break;
							default:return order_section[i]+"输入有问题呢~(副露格式不对)";
						}
					}
			}
		}
	}
	if((fulu.length>0||ming_kong.length>0)&&(reached||wreached||first_turn))
		return "条件有问题呢~";
	if(an_kong.length>0&&first_turn)
		return "条件有问题呢~";
	if(!reached&&!wreached&&yifa)
		return "条件有问题呢~";
	if(linshang&&(ming_kong.length==0)&&(an_kong.length==0))
		return "条件有问题呢~";
	if((hand_amount(hand)+1+3*fulu.length+3*ming_kong.length+3*an_kong.length)>14)
		return order_string+"的牌理:\n"+"牌型有问题呢……";
	if(!valid_paixing(hand,hold,fulu,ming_kong,an_kong,removed_section[0]))
		return "牌型有问题呀！";
	return order_string+"的牌理:\n"+paili_count(hand,hold,dora,red_dora,li_dora,bei_dora,fulu,ming_kong,an_kong,tsumo,seat,ground_wind,reached,wreached,yifa,last_turn,linshang,qiangkong,first_turn,removed_section[0]);
}

function mianzi_decompose(hand,hold,last_i=0,last_j=0,last_step=0)
{
	let mianzi_list=[];
	let card_amount=0;
	for(let i=0;i<3;++i)
		for(let j=0;j<9;++j)
			card_amount+=hand[i][j];
	if(hold>=0&&hold<27)
		card_amount+=1;
	if(card_amount==0)
		return [[]];
	if(card_amount%3==1)
		return "error";
	let i,j;
	for(i=0;i<3;++i)
	{
		for(j=0;j<9;++j)
			if(hand[i][j]>=1)
				break;
		if(hand[i][j]>=1)
			break;
	}
	if(hand[i][j]>=3&&(i!=last_i||j!=last_j||last_step<=1))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=3;
		let other_mianzi=mianzi_decompose(hand_copy,hold,i,j,1);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+1)+(j+1)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(hand[i][j]>=2&&hold==(i*9+j)&&(i!=last_i||j!=last_j||last_step<=2))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=2;
		let other_mianzi=mianzi_decompose(hand_copy,-1,i,j,2);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+1)+mpsz[i]+"+"+(j+1)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(card_amount%3==2&&hand[i][j]>=2&&(i!=last_i||j!=last_j||last_step<=3))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=2;
		let other_mianzi=mianzi_decompose(hand_copy,hold,i,j,3);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+1)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(card_amount%3==2&&hold==(i*9+j)&&(i!=last_i||j!=last_j||last_step<=4))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=1;
		let other_mianzi=mianzi_decompose(hand_copy,-1,i,j,4);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+mpsz[i]+"+"+(j+1)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(i!=3&&j<7&&hand[i][j+1]>=1&&hand[i][j+2]>=1&&(i!=last_i||j!=last_j||last_step<=5))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=1;
		hand_copy[i][j+1]-=1;
		hand_copy[i][j+2]-=1;
		let other_mianzi=mianzi_decompose(hand_copy,hold,i,j,5);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+2)+(j+3)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(i!=3&&j<7&&hand[i][j+1]>=1&&hold==(i*9+(j+2))&&(i!=last_i||j!=last_j||last_step<=6))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=1;
		hand_copy[i][j+1]-=1;
		let other_mianzi=mianzi_decompose(hand_copy,-1,i,j,6);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+2)+mpsz[i]+"+"+(j+3)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(i!=3&&j<7&&hand[i][j+2]>=1&&hold==(i*9+(j+1))&&(i!=last_i||j!=last_j||last_step<=7))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=1;
		hand_copy[i][j+2]-=1;
		let other_mianzi=mianzi_decompose(hand_copy,-1,i,j,7);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+3)+mpsz[i]+"+"+(j+2)+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(i!=3&&j>0&&j<8&&hand[i][j+1]>=1&&hold==(i*9+(j-1))&&(i!=last_i||j!=last_j||last_step<=8))
	{
		let hand_copy=JSON.parse(JSON.stringify(hand));
		hand_copy[i][j]-=1;
		hand_copy[i][j+1]-=1;
		let other_mianzi=mianzi_decompose(hand_copy,-1,i,j,8);
		if(other_mianzi!="error")
			for(let k=0;k<other_mianzi.length;++k)
			{
				other_mianzi[k].unshift(""+(j+1)+(j+2)+mpsz[i]+"+"+j+mpsz[i]);
				mianzi_list.unshift(other_mianzi[k]);
			}
	}
	if(JSON.stringify(mianzi_list)=="[]"||JSON.stringify(mianzi_list)=="[[]]")
		return "error";
	return mianzi_list;
}

function zi_decompose(hand,hold)
{
	let zi_list=[];
	let zi_amount=0;
	let quetou=false;
	for(let i=0;i<7;++i)
	{
		if(hand[3][i]>3)
			return "error";
		if(hand[3][i]==3)
			zi_list.push(""+(i+1)+(i+1)+(i+1)+"z");
		if(hand[3][i]==2&&hold==27+i)
			zi_list.push(""+(i+1)+(i+1)+"z"+"+"+(i+1)+"z");
		if(hand[3][i]==2&&(hold!=27+i)&&quetou)
			return "error";
		if(hand[3][i]==1&&(hold==27+i)&&quetou)
			return "error";
		if(hand[3][i]==2&&(hold!=27+i)&&(!quetou))
		{
			zi_list.push(""+(i+1)+(i+1)+"z");
			quetou=true;
		}
		if(hand[3][i]==1&&(hold==27+i)&&(!quetou))
		{
			zi_list.push(""+(i+1)+"z"+"+"+(i+1)+"z");
			quetou=true;
		}
		if(hand[3][i]==1&&hold!=27+i)
			return "error";
		zi_amount+=hand[3][i];
	}
	if(hold>=27&&hold<36)
		zi_amount+=1;
	if(zi_amount%3==1)
		return "error";
	return zi_list;
}

function is_jiulian(hand)
{
	let i;
	for(i=0;i<3;++i)
		if(hand[i][0]>0)
			break;
	if(i==3)
		return false;
	return (mahjong_distance([3,1,1,1,1,1,1,1,3],i,hand)<=0)&&(mahjong_distance(hand[i],0,[[3,1,1,1,1,1,1,1,3]])==1);
}

function add_to_hand(hand,hold)
{
	let add_hand=JSON.parse(JSON.stringify(hand));
	if(hold>=0&&hold<34)
		if(hand[Math.floor(hold/9)][hold%9]==4)
			return basic_hand;
		else
			add_hand[Math.floor(hold/9)][hold%9]+=1;
	return add_hand;
}

function paixing(hand,hold)
{
	let add_hand=add_to_hand(hand,hold);
	if(hand_amount(add_hand)==0)
		return "未";
	if(kuosi_santen(add_hand)<=0)
		return "国";
	if(is_jiulian(add_hand))
		return "九";
	let the_decompose=[mianzi_decompose(hand,hold),zi_decompose(hand,hold)];
	if(the_decompose[0]!="error"&&the_decompose[1]!="error")
		return the_decompose;
	if(qidui_santen(add_hand)<=0)
		return "七"
	return "未";
}

function standard_mianzi(mianzi_a)
{
	if(typeof(mianzi_a)=="number")
		return ""+(mianzi_a%9+1)+(mianzi_a%9+1)+(mianzi_a%9+1)+mpsz[Math.floor(mianzi_a/9)];
	if(mianzi_a.length<=4)
		return mianzi_a;
	if(mianzi_a.length==5)
		return mianzi_a[0]+mianzi_a.substr(3);
	switch(mianzi_a.substr(0,2))
	{
		case "12":
		case "13":return "123"+mianzi_a[2];
		case "23":if(mianzi_a[4]=="1") return "123"+mianzi_a[2]; else return "234"+mianzi_a[2];
		case "24":return "234"+mianzi_a[2];
		case "34":if(mianzi_a[4]=="2") return "234"+mianzi_a[2]; else return "345"+mianzi_a[2];
		case "35":return "345"+mianzi_a[2];
		case "45":if(mianzi_a[4]=="3") return "345"+mianzi_a[2]; else return "456"+mianzi_a[2];
		case "46":return "456"+mianzi_a[2];
		case "56":if(mianzi_a[4]=="4") return "456"+mianzi_a[2]; else return "567"+mianzi_a[2];
		case "57":return "567"+mianzi_a[2];
		case "67":if(mianzi_a[4]=="5") return "567"+mianzi_a[2]; else return "678"+mianzi_a[2];
		case "68":return "678"+mianzi_a[2];
		case "78":if(mianzi_a[4]=="6") return "678"+mianzi_a[2]; else return "789"+mianzi_a[2];
		case "79":
		case "89":return "789"+mianzi_a[2];
		default:return mianzi_a.substr(0,2)+mianzi_a.substr(4);
	}
}

function valid_paixing(hand,hold,fulu,ming_kong,an_kong,remove)
{
	let all_hand=add_to_hand(hand,hold);
	let max_hand=14+ming_kong.length+an_kong.length
	if((hand_amount(all_hand)==0)||(hand_amount(all_hand)>max_hand))
		return false;
	for(let i=0;i<ming_kong.length;++i)
		for(let j=0;j<4;++j)
		{
			all_hand=add_to_hand(all_hand,ming_kong[i]);
			if((hand_amount(all_hand)==0)||(hand_amount(all_hand)>max_hand))
				return false;
		}
	for(let i=0;i<an_kong.length;++i)
		for(let j=0;j<4;++j)
		{
			all_hand=add_to_hand(all_hand,an_kong[i]);
			if((hand_amount(all_hand)==0)||(hand_amount(all_hand)>max_hand))
				return false;
		}
	for(let i=0;i<fulu.length;++i)
		for(let j=0;j<fulu[i].length-1;++j)
		{
			all_hand=add_to_hand(all_hand,convert_to_hold(fulu[i][j]+fulu[i][fulu[i].length-1]));
			if((hand_amount(all_hand)==0)||(hand_amount(all_hand)>max_hand))
				return false;
		}
	for(let i=0;i<4;++i)
		for(let j=0;j<((i==3)?7:9);++j)
			if(all_hand[i][j]+remove[i][j]>4)
				return false;
	return true;
}

function paili_count(hand,hold,dora=0,red_dora=0,li_dora=0,bei_dora=0,fulu=[],ming_kong=[],an_kong=[],tsumo=false,seat=7,ground_wind=7,reached=false,wreached=false,yifa=false,last_turn=false,linshang=false,qiangkong=false,first_turn=false,remove=basic_hand)
{
	let the_paixing=paixing(hand,hold);	//拆牌结果
	if((hand_amount(hand)+((hold>=0&&hold<34)?1:0)+3*fulu.length+3*ming_kong.length+3*an_kong.length)!=14)
		the_paixing="未";
	switch(the_paixing)
	{
		case "国":return kuosi_count(hand,tsumo,seat,ground_wind,first_turn);
		case "九":return jiulian_count(hand,hold,tsumo,seat,ground_wind,first_turn);
		case "七":return qidui_count(add_to_hand(hand,hold),dora,red_dora,li_dora,bei_dora,tsumo,seat,ground_wind,reached,wreached,yifa,last_turn,first_turn,linshang);
		case "未":
		{
			let residue_mountain=JSON.parse(JSON.stringify(basic_mountain));
			for(let i=0;i<4;++i)
				for(let j=0;j<((i==3)?7:9);++j)
					residue_mountain[i][j]-=(hand[i][j]+remove[i][j]);
			if(hold>=0)
				residue_mountain[Math.floor(hold/9)][hold%9]-=1;
			for(let k=0;k<fulu.length;++k)
			{
				let the_fulu_cards=convert_to_card(fulu[k]);
				for(let i=0;i<4;++i)
					for(let j=0;j<((i==3)?7:9);++j)
						residue_mountain[i][j]-=the_fulu_cards[i][j];
			}
			for(let k=0;k<ming_kong.length;++k)
				residue_mountain[Math.floor(ming_kong[k]/9)][ming_kong[k]%9]-=4;
			for(let k=0;k<an_kong.length;++k)
				residue_mountain[Math.floor(an_kong[k]/9)][an_kong[k]%9]-=4;
			return paili(add_to_hand(hand,hold),residue_mountain)+"\n"+santen_reply(add_to_hand(hand,hold),residue_mountain);}
	}
	//基本牌型
	let yaojiu_amount=0,zi_amount=0,lv_amount=0,color_list=[false,false,false,false],zike_list=[false,false,false,false,false,false,false,false],ziquetou_list=[false,false,false,false,false,false,false,false],zhong_anke=0,yaojiu_anke=0,zi_anke=0,zhong_mingke=0,yaojiu_mingke=0,zi_mingke=0,zhong_ankong=0,yaojiu_ankong=0,zi_ankong=0,zhong_mingkong=0,yaojiu_mingkong=0,zi_mingkong=0,yaojiu_quetou=false,zi_quetou=false,yaojiu_shun=0,menqing=true,tingpaifu=false;
	//全部变量
	for(let i=0;i<fulu.length;++i)
	{
		if(fulu[i][3]!="z")
		{
			switch(fulu[i].substr(0,3))
			{
				case "111":
				case "999":yaojiu_amount+=3;yaojiu_mingke+=1;break;
				case "123":
				case "789":yaojiu_amount+=1;yaojiu_shun+=1;break;
				default:if(fulu[i][0]==fulu[i][1]) zhong_mingke+=1;		
			}
			color_list[mpsz.indexOf(fulu[i][3])]=true;
		}
		else
		{
			zi_mingke+=1;
			zi_amount+=3;
			zike_list[fulu[i][0]-"1"]=true;
			color_list[3]=true;
			if(fulu[i][0]=="5")
				lv_amount+=3;
		}
		if(fulu[i][3]=="s")
		{
			switch(fulu[i].substr(0,3))
			{
				case "222":
				case "333":
				case "444":
				case "234":
				case "666":
				case "888":lv_amount+=3;		
			}
		}
	}
	if(fulu.length>0)
		menqing=false;
	//副露统计
	for(let i=0;i<ming_kong.length;++i)
	{
		if(ming_kong[i]/9>=3)
		{
			zi_mingkong+=1;
			zi_amount+=3;
			zike_list[ming_kong[i]%9]=true;
			if(ming_kong[i]%9==5)
				lv_amount+=3;
			continue;
		}
		color_list[Math.floor(ming_kong[i]/9)]=true;
		if(ming_kong[i]%9==0||ming_kong[i]%9==8)
		{
			yaojiu_mingkong+=1;
			yaojiu_amount+=3;
			continue;
		}
		if(ming_kong[i]==19||ming_kong[i]==20||ming_kong[i]==21||ming_kong[i]==23||ming_kong[i]==25)
			lv_amount+=3;
		zhong_mingkong+=1;
	}
	if(ming_kong.length>0)
		menqing=false;
	//明杠统计
	for(let i=0;i<an_kong.length;++i)
	{
		if(an_kong[i]/9>=3)
		{
			zi_ankong+=1;
			zi_amount+=3;
			zike_list[an_kong[i]%9]=true;
			if(an_kong[i]%9==5)
				lv_amount+=3;
			continue;
		}
		color_list[Math.floor(an_kong[i]/9)]=true;
		if(an_kong[i]%9==0||an_kong[i]%9==8)
		{
			yaojiu_ankong+=1;
			yaojiu_amount+=3;
			continue;
		}
		if(an_kong[i]==19||an_kong[i]==20||an_kong[i]==21||an_kong[i]==23||an_kong[i]==25)
			lv_amount+=3;
		zhong_ankong+=1;
	}
	//暗杠统计
	for(let i=0;i<4;++i)
		for(let j=0;j<((i==3)?7:9);++j)
			if(hand[i][j]>0)
			{
				if(i==3)
					zi_amount+=hand[i][j];
				else if(j==0||j==8)
					yaojiu_amount+=hand[i][j];
				color_list[i]=true;
			}
	lv_amount+=hand[2][1]+hand[2][2]+hand[2][3]+hand[2][5]+hand[2][7]+hand[3][5];
	//手牌幺九字绿统计
	if(hold/9>=3)
	{
		zi_amount+=1;
		if(hold%9==5)
			lv_amount+=1;
	}
	if(hold>=0&&(hold%9==0||hold%9==8))
		yaojiu_amount+=1;
	if(hold>=0)
		color_list[Math.floor(hold/9)]=true;
	if(Math.floor(hold/9)==2&&((hold%9==1)||(hold%9==2)||(hold%9==3)||(hold%9==5)||(hold%9==7)))
		lv_amount+=1;
	//和牌幺九字绿统计
	for(let i=0;i<the_paixing[1].length;++i)
		switch(the_paixing[1][i].length)
		{
			case 5:tingpaifu=true;
			case 3:ziquetou_list[the_paixing[1][i][0]-"1"]=true; zi_quetou=true; break;
			case 4:zike_list[the_paixing[1][i][0]-"1"]=true; zi_anke+=1; break;
			case 6:zike_list[the_paixing[1][i][0]-"1"]=true; if(tsumo) zi_anke+=1; else zi_mingke+=1; break;
		}
	//手牌字暗刻、明刻(荣和)、雀头统计
	let yizhong_list=[],yiman_list=[],fu=20,fan=dora+red_dora+li_dora+bei_dora,beiyiman=0;
	if(reached){	yizhong_list.push("立直"); fan+=1;}
	if(yifa){	yizhong_list.push("一発"); fan+=1;}
	if(menqing&&tsumo){	yizhong_list.push("門前清自摸和"); fan+=1;}
	if(yaojiu_amount==0&&zi_amount==0){	yizhong_list.push("斷幺九"); fan+=1;}
	if(zike_list[seat]){	yizhong_list.push("自風"+z_crr[seat]);	fan+=1;}
	if(zike_list[ground_wind]){	yizhong_list.push("場風"+z_crr[ground_wind]);	fan+=1;}
	if(zike_list[4]){	yizhong_list.push("役牌"+z_crr[4]);	fan+=1;}
	if(zike_list[5]){	yizhong_list.push("役牌"+z_crr[5]);	fan+=1;}
	if(zike_list[6]){	yizhong_list.push("役牌"+z_crr[6]);	fan+=1;}
	if(linshang)	{	yizhong_list.push("嶺上開花");	fan+=1;}
	if(last_turn&&tsumo)	{	yizhong_list.push("海底摸月");	fan+=1;}
	if(last_turn&&!tsumo)	{	yizhong_list.push("河底撈魚");	fan+=1;}
	if(qiangkong)	{	yizhong_list.push("槍槓");	fan+=1;}
	if(wreached)	{	yizhong_list.push("兩立直");	fan+=2;}
	if((yaojiu_amount+zi_amount)>=14&&zi_amount>0)	{	yizhong_list.push("混老頭");	fan+=2;}
	if((yaojiu_ankong+yaojiu_mingkong+zi_ankong+zi_mingkong+zhong_ankong+zhong_mingkong)==3){yizhong_list.push("三槓子");	fan+=2;}
	if((zike_list[4]&&zike_list[5]&&ziquetou_list[6])||(zike_list[5]&&zike_list[6]&&ziquetou_list[4])||(zike_list[6]&&zike_list[4]&&ziquetou_list[5]))	{	yizhong_list.push("小三元");	fan+=2;}
	if(color_list[3]&&(((!color_list[0])&&(!color_list[1]))||((!color_list[1])&&(!color_list[2]))||((!color_list[2])&&(!color_list[0]))))	{	yizhong_list.push("混一色");	fan+=((menqing)?3:2);}
	if((!color_list[3])&&(((!color_list[0])&&(!color_list[1]))||((!color_list[1])&&(!color_list[2]))||((!color_list[2])&&(!color_list[0]))))	{	yizhong_list.push("清一色");	fan+=((menqing)?6:5);}
	if(first_turn&&tsumo&&(seat==0)){	yiman_list.push("天和"); beiyiman+=1;}
	if(first_turn&&tsumo&&(seat!=0)){	yiman_list.push("地和"); beiyiman+=1;}
	if((zhong_ankong+yaojiu_ankong+zi_ankong+zhong_mingkong+yaojiu_mingkong+zi_mingkong)==4){	yiman_list.push("四槓子");beiyiman+=1;	}
	if(yaojiu_amount==14){	yiman_list.push("清老頭");	beiyiman+=1;}
	if(zi_amount==14){	yiman_list.push("字一色");	beiyiman+=1;}
	if(zike_list[0]&&zike_list[1]&&zike_list[2]&&zike_list[3]){	yiman_list.push("大四喜");	beiyiman+=2;}
	if((zike_list[0]&&zike_list[1]&&zike_list[2]&&ziquetou_list[3])||(zike_list[0]&&zike_list[1]&&ziquetou_list[2]&&zike_list[3])||(zike_list[0]&&ziquetou_list[1]&&zike_list[2]&&zike_list[3])||(ziquetou_list[0]&&zike_list[1]&&zike_list[2]&&zike_list[3])){	yiman_list.push("小四喜"); beiyiman+=1;}
	if(zike_list[4]&&zike_list[5]&&zike_list[6])	{	yiman_list.push("大三元"); beiyiman+=1;}
	if(lv_amount==14){	yiman_list.push("綠一色");	beiyiman+=1;}
	fu+=zhong_mingke*2+zhong_anke*4+zhong_mingkong*8+zhong_ankong*16+(yaojiu_mingke+zi_mingke)*4+(yaojiu_anke+zi_anke)*8+(yaojiu_mingkong+zi_mingkong)*16+(yaojiu_ankong+zi_ankong)*32;
	if(ziquetou_list[seat])	fu+=2;
	if(ziquetou_list[ground_wind])	fu+=2;
	if(ziquetou_list[4])	fu+=2;
	if(ziquetou_list[5])	fu+=2;
	if(ziquetou_list[6])	fu+=2;
	if(menqing&&(!tsumo))	fu+=10;
	if((!menqing)&&tsumo)	fu+=2;
	//固定役种(无需拆牌)
	let yizhong_cases=[],yiman_cases=[],fu_cases=[],tingpaifu_cases=[],fan_cases=[],beiyiman_cases=[],jizhundian_cases=[];
	for(let i=0;i<the_paixing[0].length;++i)
	{
		let case_yizhong_list=yizhong_list.concat(),case_yiman_list=yiman_list.concat(),case_fu=fu,case_tingpaifu=tingpaifu,case_fan=fan,case_beiyiman=beiyiman;
		let case_yaojiu_shun=0,case_zhong_anke=0,case_zhong_mingke=0,case_yaojiu_anke=0,case_yaojiu_mingke=0,case_yaojiu_quetou=yaojiu_quetou;
		for(let j=0;j<the_paixing[0][i].length;++j)
		{
			switch(the_paixing[0][i][j].length)
			{
				case 5:case_tingpaifu=true;
				case 3:if(the_paixing[0][i][j][0]=="1"||the_paixing[0][i][j][0]=="9") case_yaojiu_quetou=true; break;
				case 4:switch(the_paixing[0][i][j].substr(0,3))
					{
						case "111":
						case "999":case_yaojiu_anke+=1;break;
						case "123":
						case "789":case_yaojiu_shun+=1;break;
						default:if(the_paixing[0][i][j][0]==the_paixing[0][i][j][1])
							case_zhong_anke+=1;
					}
					break;
				case 6:switch(the_paixing[0][i][j].substr(0,2))
					{
						case "11":
						case "99":if(tsumo) case_yaojiu_anke+=1; else case_yaojiu_mingke+=1; break;
						case "12":
						case "89":
						case "13":
						case "79":case_yaojiu_shun+=1;
						case "24":
						case "35":
						case "46":
						case "57":
						case "68":case_tingpaifu=true; break;
						case "23":if(the_paixing[0][i][j][4]=="1") case_yaojiu_shun+=1; break;
						case "78":if(the_paixing[0][i][j][4]=="9") case_yaojiu_shun+=1; break;
						default:if(the_paixing[0][i][j][0]==the_paixing[0][i][j][1])
							{if(tsumo) case_zhong_anke+=1; else case_zhong_mingke+=1;}
					}
					break;
			}
		}
		//统计拆牌情况
		if(menqing&&case_zhong_anke==0&&case_yaojiu_anke==0&&case_zhong_mingke==0&&case_yaojiu_mingke==0&&zhong_anke==0&&yaojiu_anke==0&&zi_anke==0&&zhong_mingke==0&&yaojiu_mingke==0&&zi_mingke==0&&zhong_ankong==0&&yaojiu_ankong==0&&zi_ankong==0&&(!case_tingpaifu)&&(!ziquetou_list[seat])&&(!ziquetou_list[ground_wind])&&(!ziquetou_list[4])&&(!ziquetou_list[5])&&(!ziquetou_list[6]))
		{	case_yizhong_list.push("平和"); case_fan+=1;}
		if((yaojiu_shun+case_yaojiu_shun+yaojiu_mingke+yaojiu_anke+yaojiu_mingkong+yaojiu_ankong+case_yaojiu_mingke+case_yaojiu_anke+zi_mingke+zi_anke+zi_mingkong+zi_ankong)==4&&(case_yaojiu_quetou||zi_quetou)&&((yaojiu_shun+case_yaojiu_shun)>0)&&zi_amount>0)
		{	case_yizhong_list.push("混全帶幺九"); case_fan+=(menqing?2:1);}
		if((zhong_anke+zhong_ankong+yaojiu_anke+yaojiu_ankong+zi_anke+zi_ankong+case_zhong_anke+case_yaojiu_anke)==3)
		{	case_yizhong_list.push("三暗刻"); case_fan+=2;}
		if((zhong_mingke+zhong_anke+zhong_mingkong+zhong_ankong+yaojiu_mingke+yaojiu_anke+yaojiu_mingkong+yaojiu_ankong+zi_mingke+zi_anke+zi_mingkong+zi_ankong+case_zhong_mingke+case_zhong_anke+case_yaojiu_mingke+case_yaojiu_anke)==4)
		{	case_yizhong_list.push("對對和"); case_fan+=2;}
		if((yaojiu_shun+case_yaojiu_shun+yaojiu_mingke+yaojiu_anke+yaojiu_mingkong+yaojiu_ankong+case_yaojiu_mingke+case_yaojiu_anke)==4&&case_yaojiu_quetou)
		{	case_yizhong_list.push("純全帶幺九"); case_fan+=(menqing?3:2);}
		if((zhong_anke+zhong_ankong+yaojiu_anke+yaojiu_ankong+zi_anke+zi_ankong+case_zhong_anke+case_yaojiu_anke)==4&&(!case_tingpaifu))
		{	case_yiman_list.push("四暗刻"); case_beiyiman+=1;}
		if((zhong_anke+zhong_ankong+yaojiu_anke+yaojiu_ankong+zi_anke+zi_ankong+case_zhong_anke+case_yaojiu_anke)==4&&case_tingpaifu)
		{	case_yiman_list.push("四暗刻單騎"); case_beiyiman+=2;}
		//无需查找具体比较
		if(menqing)
		{
			if(((the_paixing[0][i].length==5)&&(((standard_mianzi(the_paixing[0][i][0])==standard_mianzi(the_paixing[0][i][1]))&&(standard_mianzi(the_paixing[0][i][2])==standard_mianzi(the_paixing[0][i][3])))||((standard_mianzi(the_paixing[0][i][0])==standard_mianzi(the_paixing[0][i][1]))&&(standard_mianzi(the_paixing[0][i][3])==standard_mianzi(the_paixing[0][i][4])))||((standard_mianzi(the_paixing[0][i][1])==standard_mianzi(the_paixing[0][i][2]))&&(standard_mianzi(the_paixing[0][i][3])==standard_mianzi(the_paixing[0][i][4])))))||(the_paixing[0][i].length==4)&&((standard_mianzi(the_paixing[0][i][0])==standard_mianzi(the_paixing[0][i][1]))&&(standard_mianzi(the_paixing[0][i][2])==standard_mianzi(the_paixing[0][i][3]))))
			{	case_yizhong_list.push("二盃口"); case_fan+=3;}
			else
				for(let j=0;j<the_paixing[0][i].length-1;++j)
					if(standard_mianzi(the_paixing[0][i][j])==standard_mianzi(the_paixing[0][i][j+1]))
						{	case_yizhong_list.push("一盃口"); case_fan+=1; break;}
		}
		//二盃口、一盃口判断
		let sanse_list={},yiqi_list={"m":[],"p":[],"s":[]};
		for(let j=0;j<the_paixing[0][i].length;++j)
		{
			let the_mianzi=standard_mianzi(the_paixing[0][i][j]);
			if(sanse_list[the_mianzi.substr(0,3)])
			{
				if(sanse_list[the_mianzi.substr(0,3)].indexOf(the_mianzi[3])<0)
				sanse_list[the_mianzi.substr(0,3)].push(the_mianzi[3]);
			}
			else
				sanse_list[the_mianzi.substr(0,3)]=[the_mianzi[3]];
			switch(the_mianzi.substr(0,3))
			{
				case "123":
				case "456":
				case "789":if(yiqi_list[the_mianzi[3]].indexOf(the_mianzi.substr(0,3))<0)
						yiqi_list[the_mianzi[3]].push(the_mianzi.substr(0,3));
			}
		}
		for(let j=0;j<fulu.length;++j)
		{
			let the_mianzi=standard_mianzi(fulu[j]);
			if(sanse_list[the_mianzi.substr(0,3)])
			{
				if((the_mianzi[3]!="z")&&(sanse_list[the_mianzi.substr(0,3)].indexOf(the_mianzi[3])<0))
				sanse_list[the_mianzi.substr(0,3)].push(the_mianzi[3]);
			}
			else
				sanse_list[the_mianzi.substr(0,3)]=[the_mianzi[3]];
			switch(the_mianzi.substr(0,3))
			{
				case "123":
				case "456":
				case "789":if(yiqi_list[the_mianzi[3]].indexOf(the_mianzi.substr(0,3))<0)
						yiqi_list[the_mianzi[3]].push(the_mianzi.substr(0,3));
			}
		}
		for(let j=0;j<ming_kong.length;++j)
		{
			let the_mianzi=standard_mianzi(ming_kong[j]);
			if(sanse_list[the_mianzi.substr(0,3)])
			{
				if((the_mianzi[3]!="z")&&(sanse_list[the_mianzi.substr(0,3)].indexOf(the_mianzi[3])<0))
				sanse_list[the_mianzi.substr(0,3)].push(the_mianzi[3]);
			}
			else
				sanse_list[the_mianzi.substr(0,3)]=[the_mianzi[3]];
		}
		for(let j=0;j<an_kong.length;++j)
		{
			let the_mianzi=standard_mianzi(an_kong[j]);
			if(sanse_list[the_mianzi.substr(0,3)])
			{
				if((the_mianzi[3]!="z")&&(sanse_list[the_mianzi.substr(0,3)].indexOf(the_mianzi[3])<0))
				sanse_list[the_mianzi.substr(0,3)].push(the_mianzi[3]);
			}
			else
				sanse_list[the_mianzi.substr(0,3)]=[the_mianzi[3]];
		}
		for(let sanse_mianzi in sanse_list)
			if(sanse_list[sanse_mianzi].length==3)
				if(sanse_mianzi[0]==sanse_mianzi[1])
				{	case_yizhong_list.push("三色同刻"); case_fan+=2; break;}
				else
				{	case_yizhong_list.push("三色同順"); case_fan+=(menqing?2:1); break;}
		for(let yiqi_mianzi in yiqi_list)
			if(yiqi_list[yiqi_mianzi].length==3)
				{	case_yizhong_list.push("一氣通貫"); case_fan+=(menqing?2:1); break;}
		//三色同顺、三色同刻、一气通贯判断
		case_fu+=case_zhong_mingke*2+case_zhong_anke*4+case_yaojiu_mingke*4+case_yaojiu_anke*8+(case_tingpaifu?2:0);
		if(menqing&&tsumo&&case_yizhong_list.indexOf("平和")<0)
			case_fu+=2;
		if(!menqing&&!tsumo&&case_fu==20)
			case_fu=30;
		case_fu=Math.ceil(case_fu/10)*10;
		yizhong_cases.push(case_yizhong_list);
		yiman_cases.push(case_yiman_list);
		fu_cases.push(case_fu);
		fan_cases.push(case_fan);
		beiyiman_cases.push(case_beiyiman);
		jizhundian_cases.push(case_fu*(2**(2+case_fan)));
	}
	let cases_sequence=[];
	for(let i=0;i<the_paixing[0].length;++i)
		cases_sequence.push(i);
	cases_sequence.sort((a,b)=>{return (beiyiman_cases[b]==beiyiman_cases[a])?((jizhundian_cases[a]>2000&&jizhundian_cases[b]>2000)?((fan_cases[b]==fan_cases[a])?(fu_cases[b]-fu_cases[a]):(fan_cases[b]-fan_cases[a])):(jizhundian_cases[b]-jizhundian_cases[a])):(beiyiman_cases[b]-beiyiman_cases[a]);});
	let max_case=cases_sequence[0];
	if(yiman_cases[max_case].length>0)
		return yiman_result(yiman_cases[max_case],beiyiman_cases[max_case],fu_cases[max_case],seat,ground_wind,tsumo);
	else
		return yizhong_result(yizhong_cases[max_case],fan_cases[max_case],fu_cases[max_case],jizhundian_cases[max_case],dora,red_dora,li_dora,bei_dora,seat,ground_wind,tsumo,menqing);
}

function yiman_result(yiman_list,beiyiman,fu,seat,ground_wind,tsumo)
{
	let yiman_string="";
	for(let i=0;i<11;++i)
		if(yiman_list.indexOf(役满列表[i])>=0)
		{
			yiman_string+=役满列表[i];
			for(let k=0;k<5-役满列表[i].length;++k)
				yiman_string+=`　`;
			yiman_string+=`	`+((役满倍数[i]==1)?"役滿":"两倍役滿")+"\n";
		}
	yiman_string+=fu+"符"+(beiyiman*13)+"翻"+`	`+beiyiman+"倍役滿\n";
	yiman_string+="("+z_crr[ground_wind]+"場"+z_crr[seat]+"家)"+`	`;
	if(seat==0)
		yiman_string+=tsumo?((16000*beiyiman)+"點all"):("銃和"+(48000*beiyiman)+"點");
	else
		yiman_string+=tsumo?("闲家"+(8000*beiyiman)+"點，亲家"+(16000*beiyiman)+"點"):("銃和"+(32000*beiyiman)+"點");
	return yiman_string;
}

function yizhong_result(yizhong_list,fan,fu,jizhundian,dora,red_dora,li_dora,bei_dora,seat,ground_wind,tsumo,menqing)
{
	if(yizhong_list.length==0)
		return "和了……咦？无役怪？[CQ:face,id=0]";
	let yizhong_string="";
	for(let i=0;i<役种列表.length;++i)
		if(yizhong_list.indexOf(役种列表[i])>=0)
		{
			yizhong_string+=役种列表[i];
			for(let k=0;k<6-役种列表[i].length;++k)
				yizhong_string+=`　`;
			yizhong_string+=`	`+(menqing?(门清役种翻数[i]+"翻"):(食下役种翻数[i]+"翻"))+"\n";
		}
	if(dora>0)
		yizhong_string+="ドラ　　　　"+`	`+dora+"翻\n";
	if(red_dora>0)
		yizhong_string+="赤ドラ　　　"+`	`+red_dora+"翻\n";
	if(li_dora>0)
		yizhong_string+="裏ドラ　　　"+`	`+li_dora+"翻\n";
	if(bei_dora>0)
		yizhong_string+="北ドラ　　　"+`	`+bei_dora+"翻\n";
	yizhong_string+=fu+"符"+fan+"翻"+`	`+满贯大小(jizhundian,fan)+"\n";
	yizhong_string+="("+z_crr[ground_wind]+"場"+z_crr[seat]+"家)"+`	`;
	jizhundian=mj_suandian(jizhundian,fan);
	if(seat==0)
		yizhong_string+=tsumo?((Math.ceil(2*jizhundian/100)*100)+"點all"):("銃和"+(Math.ceil(6*jizhundian/100)*100)+"點");
	else
		yizhong_string+=tsumo?("闲家"+(Math.ceil(jizhundian/100)*100)+"點，亲家"+(Math.ceil(2*jizhundian/100)*100)+"點"):("銃和"+(Math.ceil(4*jizhundian/100)*100)+"點");
	return yizhong_string;
}

function 满贯大小(jizhundian,fan)
{
	if(jizhundian<=2000)
		return "";
	switch(fan)
	{
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:return "滿貫";
		case 6:
		case 7:return "跳滿";
		case 8:
		case 9:
		case 10:return "倍滿";
		case 11:
		case 12:return "三倍滿";
		default:return "累計役滿";
	}
}

function mj_suandian(jizhundian,fan)
{
	if(jizhundian<=2000)
		return jizhundian;
	switch(fan)
	{
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:return 2000;
		case 6:
		case 7:return 3000;
		case 8:
		case 9:
		case 10:return 4000;
		case 11:
		case 12:return 6000;
		default:return 8000*Math.floor(fan/13);
	}
}