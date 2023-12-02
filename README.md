# Algrim Chat

Algrim Chat is a chat styling fabric mod for clients.

## About

The mod applies custom styling to server messages through use of "patterns". These patterns tell the mod which messages
should be styled, and how to style them.

## Dependencies

- Minecraft: `1.20.0+`
- [MaLiLib](https://www.curseforge.com/minecraft/mc-mods/malilib): `0.16.0+`
- [fabric-language-kotlin](https://modrinth.com/mod/fabric-language-kotlin): `1.9.10+`

## Configuration

Config GUI can be opened with `R,C`, by default.

## Patterns

Patterns consist of three components:

- ### Literal

  Literal pattern parts are the simplest. They match characters exactly (with a few exceptions).

  #### Example Patterns:
  | Pattern           | Message            | Matches |
  |-------------------|--------------------|:-------:|
  | `You leveled up!` | "You leveled up!"  |    ✅    |
  | `<Player6> Hello` | "\<Player6> Hello" |    ✅    |
  | `<Player6> Hello` | "\<Player6> Bye"   |    ❌    |
  | `<Player6> Hello` | "\<Player2> Hello" |    ❌    |

  They're great for matching static messages that don't change, but not too useful on their own in terms of
  chat on a server where player names and message content change.
  <br><br>
  Some special characters may also be mistaken as other pattern parts, avoid this by escaping them with `\​`

- ### Regex

  Regex pattern parts are denoted by a `/` at the beginning and end.

  #### Example Patterns:
  | Pattern                          | Message                          | Matches | Note                                                                        |
  |----------------------------------|----------------------------------|:-------:|-----------------------------------------------------------------------------|
  | <nobr>`/You leveled up!/`</nobr> | <nobr>"You leveled up!"</nobr>   |    ✅    | While regex can match static messages, it's often simpler to use a Literal. |
  | <nobr>`/.*/`</nobr>              | <nobr>"\<Player6> Hello"</nobr>  |    ✅    | Will technically match all messages.                                        |
  | <nobr>`</\\w+/> /.*/`</nobr>     | <nobr>"\<Player6> Bye"</nobr>    |    ✅    | Here we are using a combination of Literal "<" and "> ", and Regex parts.   |

- ### Style

  Style pattern parts apply style to messages (**Finally!!!**) and are split into two
  sections: `[Style, Overrides]` and `(Content)`
  <br><br>
    - [**Style Overrides**](#style-overrides) are a comma separated list of styles.
    - **Content** consists of Literal or Regex components.

  #### Example Patterns:

  | Pattern                                            | Message                          | Matches | Note                                                        |
  |----------------------------------------------------|----------------------------------|:-------:|-------------------------------------------------------------|
  | <nobr>`[green](You leveled up!)`</nobr>            | <nobr>"You leveled up!"</nobr>   |    ✅    | Styles the message green.                                   |
  | <nobr>`<[#00FFFF](/\\w+/)> [white,i](/.*/)`</nobr> | <nobr>"\<Player6> Hello"</nobr>  |    ✅    | Styles the player's name in aqua and their message *white*. |

## Style Overrides

#### The valid Style Overrides are:

|                                    | Override Name(s)      |
|:----------------------------------:|-----------------------|
|              **Bold**              | `bold`, `b`           |
|             *Italics*              | `italic`, `it`, `i`   |
|       <ins>Underlined</ins>        | `underline`, `u`      |
|         ~~Strikethrough~~          | `strikethrough`, `s`  |
|       ͅO͆b̼f̧u̺s̓c͠a̿t̝e̿d̮͒       | `obfuscate`, `o`      |
| <font color="yellow">Colour</font> | `#<hex>`, `<mc name>` |

Styles can be removed by prefixing `!` to the Override Name. `!` on its own will strip all styles.  
ex: `[!bold](/.*/)` remove bold.  
or: `[!](/.*/)` remove all styling.
