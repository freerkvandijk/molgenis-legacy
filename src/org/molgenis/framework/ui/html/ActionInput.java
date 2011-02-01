package org.molgenis.framework.ui.html;

/**
 * The ActionInput defines action buttons.
 */
public class ActionInput extends HtmlInput
{
	public enum Type
	{
		//save current record
		SAVE("Save"), 
		//cancel current action (closes popup window, refreshes opener window)
		CANCEL("Cancel"), 
		//goto next screen
		NEXT("Next"), 
		//close current screen
		CLOSE("Close"), 
		//automatically close current dialoge
		AUTOCLOSE("Autoclose"),
		//a custom action that requires you to set label, tooltip and javascript yourself
		CUSTOM(
				"Set Label, Tooltip, and JavaScriptAction yourself");

		private Type(String tag)
		{
			this.tag = tag;
		}

		public final String tag;

		public String toString()
		{
			return tag;
		}
	}

	/** Path to an icon image */
	private String icon;

	/** Type of submit */
	private Type type;

	/** JavaScript action */
	private String JavaScriptAction;

	// constructor(s)
	/**
	 * Default constructor, type is submit
	 */
	public ActionInput(String name)
	{
		this(name, Type.CUSTOM);
	}
	
	public ActionInput(String name, String label)
	{
		this(name, Type.CUSTOM);
		this.setLabel(label);
	}

	/**
	 * Create a new instance of ActionInput.
	 * 
	 * @param name
	 *            name of the input.
	 * @param type
	 *            type of the input. @see Type
	 */
	public ActionInput(String name, Type type)
	{
		super(name, type);
		this.setType(type);
		this.setLabel(type.toString());
		this.setTooltip(type.toString());
	}

	public ActionInput(Type select_target)
	{
		this(select_target.toString());
		this.setLabel(select_target.toString().replace("_"," "));
	}

	// HtmlInput overloads
	@Override
	public String toHtml()
	{
		StringBuffer input = new StringBuffer("");

		// TODO: apparantly this can be disabled.
		if (getIcon() != null)
		{
			input.append("<img class=\"edit_button\" src=\"" + getIcon() + "\" alt=\"" + getLabel() + "\" onclick=\""
					+ getJavaScriptAction() + "\" title=\"" + this.getTooltip() + "\" id=\"" + this.getId()
					+ "\" style=\"" + this.getStyle() + "\" " + tabIndex + " />");
		}
		else
		{
			input.append("<input type=\"submit\" onclick=\"" + getJavaScriptAction() + "\" title=\""
					+ this.getTooltip() + "\" id=\"" + this.getId() + "\"" + "value=\"" + this.getLabel()
					+ "\" style=\"" + this.getStyle() + "\" " + tabIndex + " />");
		}

		return input.toString();
	}

	// attribute methods
	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getJavaScriptAction()
	{
		if (JavaScriptAction == null)
		{
			if (this.type == Type.SAVE)
			{
				StringBuffer jScript = new StringBuffer();
				jScript.append("if( validateForm(molgenis_popup,molgenis_required) ) { if( window.opener.name == '' ){ window.opener.name = 'molgenis'+Math.random();} document.forms.molgenis_popup.target = window.opener.name; document.forms.molgenis_popup.submit(); window.close();} else return false;");
				return jScript.toString();
			}
			else if (this.type == Type.NEXT)
			{
				StringBuffer jScript = new StringBuffer();
				jScript.append("if( validateForm(molgenis_popup,molgenis_required) ) { if( window.opener.name == '' ){ window.opener.name = 'molgenis'+Math.random();} document.forms.molgenis_popup.__show.value='popup'; document.forms.molgenis_popup.submit();} else return false;");
				return jScript.toString();
			}
			else if (this.type == Type.CLOSE)
			{
				return "window.close();";
			}
			else if (this.type == Type.CUSTOM)
			{
				return "__action.value = \'"+getName()+"'; return true;";
			}
		}
		return JavaScriptAction;
	}

	public void setJavaScriptAction(String javaScriptAction)
	{
		JavaScriptAction = javaScriptAction;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}
	
	@Override
	public String getLabel()
	{
		if(super.getLabel() == super.getValue())
			return getName();
		return super.getLabel();
	}

	public String getIconHtml()
	{
		// TODO Auto-generated method stub
		return "<img src=\""+this.getIcon()+"\"/>";
	}

	public String toIconHtml()
	{
		return "<img class=\"edit_button\" src=\""+getIcon()+"\" title=\""+getLabel()+"\" onClick=\""+this.getJavaScriptAction()+"\">";
		//<img class="edit_button" src="generated-res/img/recordview.png" title="view record" alt="edit${offset}" onClick="setInput('${screen.name}_form','_self','','${screen.name}','recordview','iframe'); document.forms.${screen.name}_form.__offset.value='${offset}'; document.forms.${screen.name}_form.submit();">${readonly}</label>
	}

	public String toLinkHtml()
	{
		return "<a title=\""+this.getDescription()+"\" onclick=\""+this.getJavaScriptAction()+"\">"+getLabel()+"</a>";
	}
}
